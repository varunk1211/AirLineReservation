package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.BookingService;
import com.VBook.AirLineReservation.Service.OtpService;
import com.VBook.AirLineReservation.model.Booking;
import com.VBook.AirLineReservation.model.Passenger;
import com.VBook.AirLineReservation.model.Users;
import com.VBook.AirLineReservation.repo.FlightRepository;
import com.VBook.AirLineReservation.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.layout.element.Image;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final FlightRepository flightRepository;
    private final UserRepo userRepository;
    private final OtpService otpService;

    public BookingController(BookingService bookingService,
                             FlightRepository flightRepository,
                             UserRepo userRepository,
                             OtpService otpService) {
        this.bookingService = bookingService;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.otpService = otpService;
    }

    @PostMapping("/book/{flightId}")
    public String bookFlight(@PathVariable Long flightId,
                             Principal principal,
                             @RequestParam List<String> passengerNames,
                             HttpSession session) {

        Users user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        // Store booking data in session
        session.setAttribute("flightId", flightId);
        session.setAttribute("userId", user.getId());
        session.setAttribute("passengerNames", passengerNames);

        // Generate and send OTP
        String otp = otpService.generateOtp();
        otpService.sendOtp(user.getEmail(), otp);
        otpService.storeOtp(user.getEmail(), otp);

        return "redirect:/otp-verification";
    }
    @GetMapping("/book/{flightId}")
    public String showBookingPage(@PathVariable Long flightId, Model model) {

        var flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        model.addAttribute("flight", flight);

        return "booking-page"; // booking.html
    }

    @GetMapping("/my-bookings")
    public String myBookings(Principal principal, Model model) {
        Users user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Booking> bookings = bookingService.getBookingsByUser(user);
        model.addAttribute("bookings", bookings);
        model.addAttribute("pageTitle", "My Bookings - VBook Airlines");
        return "my-bookings";
    }

    @GetMapping("/ticket/{bookingId}")
    public String showTicket(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.getBookingById(bookingId);
        model.addAttribute("booking", booking);
        model.addAttribute("pageTitle", "Ticket - VBook Airlines");
        return "ticket";
    }

    @GetMapping("/download-ticket/{bookingId}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.setMargins(10, 10, 10, 10);

        // Main ticket container with border
        Div ticketDiv = new Div()
                .setBorder(new SolidBorder(ColorConstants.BLACK, 2))
                .setPadding(15)
                .setBackgroundColor(ColorConstants.WHITE);

        // Header
        Div headerDiv = new Div()
                .setBackgroundColor(ColorConstants.BLUE)
                .setPadding(10)
                .setMarginBottom(10);
        Paragraph header = new Paragraph("VBook Airlines - Electronic Ticket")
                .setFontSize(24)
                .setBold()
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);
        headerDiv.add(header);
        ticketDiv.add(headerDiv);

        // Booking Reference
        Paragraph ref = new Paragraph("Booking Reference: " + booking.getBookingReference())
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        ticketDiv.add(ref);

        // Flight Details in a styled table
        Table flightTable = new Table(new float[]{1, 1, 1, 1});
        flightTable.setWidth(UnitValue.createPercentValue(100));
        flightTable.setBorder(Border.NO_BORDER);
        flightTable.setMarginBottom(10);

        // Row 1
        addStyledCell(flightTable, "Flight Number", booking.getFlight().getFlightNumber(), ColorConstants.LIGHT_GRAY);
        addStyledCell(flightTable, "Airline", booking.getFlight().getAirlineName(), ColorConstants.LIGHT_GRAY);
        addStyledCell(flightTable, "From", booking.getFlight().getSource(), ColorConstants.LIGHT_GRAY);
        addStyledCell(flightTable, "To", booking.getFlight().getDestination(), ColorConstants.LIGHT_GRAY);

        // Row 2
        addStyledCell(flightTable, "Departure", booking.getFlight().getDepartureTime().toString(), ColorConstants.WHITE);
        addStyledCell(flightTable, "Arrival", booking.getFlight().getArrivalTime().toString(), ColorConstants.WHITE);
        addStyledCell(flightTable, "Total Amount", "$" + booking.getTotalAmount(), ColorConstants.GREEN);
        addStyledCell(flightTable, "Status", "CONFIRMED", ColorConstants.GREEN);

        ticketDiv.add(flightTable);

        // Passengers
        Paragraph passTitle = new Paragraph("Passengers:")
                .setFontSize(14)
                .setBold()
                .setUnderline()
                .setMarginBottom(5);
        ticketDiv.add(passTitle);
        for (Passenger p : booking.getPassengers()) {
            ticketDiv.add(new Paragraph("• " + p.getFullName()).setFontSize(12));
        }

        ticketDiv.add(new Paragraph("\n"));

        // Important Notes and Terms in columns
        Table infoTable = new Table(new float[]{1, 1});
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setBorder(Border.NO_BORDER);

        // Left column: Important Notes
        Cell notesCell = new Cell().setBorder(Border.NO_BORDER).setPadding(5);
        notesCell.add(new Paragraph("Important Notes:").setFontSize(12).setBold());
        notesCell.add(new Paragraph("• Arrive 2 hours before departure").setFontSize(10));
        notesCell.add(new Paragraph("• Valid ID required").setFontSize(10));
        notesCell.add(new Paragraph("• Check-in closes 45 min prior").setFontSize(10));
        infoTable.addCell(notesCell);

        // Right column: Terms
        Cell termsCell = new Cell().setBorder(Border.NO_BORDER).setPadding(5);
        termsCell.add(new Paragraph("Terms & Conditions:").setFontSize(12).setBold());
        termsCell.add(new Paragraph("• Non-refundable").setFontSize(10));
        termsCell.add(new Paragraph("• Changes 24h advance").setFontSize(10));
        termsCell.add(new Paragraph("• Follow airline rules").setFontSize(10));
        infoTable.addCell(termsCell);

        ticketDiv.add(infoTable);

        // Barcode section
        Div barcodeDiv = new Div()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10);
        Paragraph barcodeLabel = new Paragraph("Scan Barcode for Verification")
                .setFontSize(12)
                .setBold();
        barcodeDiv.add(barcodeLabel);

        Barcode128 barcode = new Barcode128(pdfDoc);
        String code = booking.getBookingReference();
        if (code == null || code.isEmpty()) {
            code = "VBK-" + booking.getId();
        }
        barcode.setCode(code);
        Image barcodeImage = new Image(barcode.createFormXObject(pdfDoc));
        barcodeImage.setWidth(250);
        barcodeImage.setHeight(60);
        barcodeDiv.add(barcodeImage);
        ticketDiv.add(barcodeDiv);

        // Footer
        Paragraph footer = new Paragraph("Thank you for flying with VBook Airlines! Safe travels. | support@vbookairlines.com")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY)
                .setMarginTop(10);
        ticketDiv.add(footer);

        document.add(ticketDiv);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileRef = booking.getBookingReference();
        if (fileRef == null || fileRef.isEmpty()) {
            fileRef = "VBK-" + booking.getId();
        }
        headers.setContentDispositionFormData("attachment", "e-ticket_" + fileRef + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(baos.toByteArray());
    }

    private void addStyledCell(Table table, String label, String value, Color bgColor) {
        Cell cell = new Cell().setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                .setBackgroundColor(bgColor)
                .setPadding(8)
                .setTextAlignment(TextAlignment.CENTER);
        cell.add(new Paragraph(label + "\n" + value).setFontSize(11).setBold());
        table.addCell(cell);
    }

    @GetMapping("/otp-verification")
    public String showOtpPage() {
        return "otp-verification";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp, HttpSession session, Principal principal, Model model) {
        Users user = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (otpService.verifyOtp(user.getEmail(), otp)) {
            // Retrieve data from session
            Long flightId = (Long) session.getAttribute("flightId");
            Long userId = (Long) session.getAttribute("userId");
            List<String> passengerNames = (List<String>) session.getAttribute("passengerNames");

            if (flightId != null && userId != null && passengerNames != null) {
                Users bookingUser = userRepository.findById(userId).orElseThrow();
                List<Passenger> passengers = passengerNames.stream().map(name -> {
                    Passenger p = new Passenger();
                    p.setFullName(name);
                    return p;
                }).toList();

                Booking booking = bookingService.createBooking(flightId, bookingUser, passengers);

                // Clear session
                session.removeAttribute("flightId");
                session.removeAttribute("userId");
                session.removeAttribute("passengerNames");

                return "redirect:/ticket/" + booking.getId();
            }
        }
        model.addAttribute("error", "Invalid OTP. Please try again.");
        return "otp-verification";
    }
}
