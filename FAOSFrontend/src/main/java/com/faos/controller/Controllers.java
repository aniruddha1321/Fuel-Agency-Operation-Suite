package com.faos.controller;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.faos.model.BookingPageView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class Controllers {
    private long gst=0;
    private long delivery_charge=0;
    private long price;
    private long surcharge=0L;
    
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Retrieve the "CUSTOMER" attribute from the session
            String userType = (String) session.getAttribute("userType");
            
            if ("CUSTOMER".equals(userType)) {
                session.invalidate(); // Invalidate session
                return "redirect:/"; // Redirect to home or another page
            }
        }
        return "index";
    }
 
    @GetMapping("/BookingLogin")
    public String login(Model model) {
        model.addAttribute("customer", new BookingPageView());
        model.addAttribute("errors", "");
        return "blogin";
    }

    @PostMapping("/logins")
    public String customerLogin(@ModelAttribute BookingPageView booking, Model model) {
        try {
            ResponseEntity<BookingPageView> response = getRestTemplate().postForEntity(
                    "http://localhost:8080/logins", booking, BookingPageView.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                BookingPageView customer = response.getBody();
                model.addAttribute("bookingPageView", customer);
                model.addAttribute("permit", "");
                model.addAttribute("customer", new BookingPageView());
                return "customer";
            } else {
                BookingPageView customer = response.getBody();
                model.addAttribute("bookingPageView", customer);
                model.addAttribute("customer", new BookingPageView());
                model.addAttribute("errors", "Bad credentials. Please try again.");
                return "blogin";
            }


        } catch (Exception e) {
            e.printStackTrace();

            model.addAttribute("customer", new BookingPageView());
            model.addAttribute("errors", "Bad credentials. Please try again.");
            model.addAttribute("error", "Bad credentials. Please try again.");
            return "blogin";
        }
    }

    @PostMapping("/booking")
    public String booking(@ModelAttribute BookingPageView booking, Model model) {
        try {
            ResponseEntity<BookingPageView> response = getRestTemplate().postForEntity(
                    "http://localhost:8080/logins", booking, BookingPageView.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                BookingPageView customer = response.getBody();
                model.addAttribute("bookingPageView", customer);
                model.addAttribute("permit", "");
                model.addAttribute("customer", new BookingPageView());
                return "booking";
            } else {
                BookingPageView customer = response.getBody();
                model.addAttribute("bookingPageView", customer);
                model.addAttribute("customer", new BookingPageView());
                model.addAttribute("errors", "Bad credentials. Please try again.");
                return "customerDashboard";
            }


        } catch (Exception e) {
            e.printStackTrace();

            model.addAttribute("customer", new BookingPageView());
            model.addAttribute("errors", "Bad credentials. Please try again.");
            model.addAttribute("error", "Bad credentials. Please try again.");
            return "customerDashboard";
        }
    }

    @PostMapping("/submit-booking")
    public String addCustomer(@ModelAttribute BookingPageView booking, Model model) {
        try {
            // --- Set delivery details and calculate base price ---
            if ("Normal".equals(booking.getDeliveryOption())) {
                booking.setDeliveryDate(LocalDate.now().plusDays(3));
                gst = 10;
                delivery_charge = 50;
                price = 1000 + gst + delivery_charge;
            } else if ("Express".equals(booking.getDeliveryOption())) {
                booking.setDeliveryDate(LocalDate.now().plusDays(1));
                gst = 10;
                delivery_charge = 100;
                price = 1000 + gst + delivery_charge;
            } else {
                throw new IllegalArgumentException("Invalid delivery option: " + booking.getDeliveryOption());
            }
            surcharge = 0; // Reset surcharge

            // --- Check if customer can book a cylinder (30-day restriction) ---
            Boolean canBook = getRestTemplate().getForEntity(
                    "http://localhost:8080/checkBooking?consumerId=" + booking.getConsumerId(),
                    Boolean.class).getBody();
            if (Boolean.FALSE.equals(canBook)) {
                model.addAttribute("permit", "Sorry, you can't book a cylinder before 30 days of your last booking.");
                return "booking"; // Stop the booking process
            }

            // --- Surcharge Logic: Apply 20% surcharge if this is the 7th booking within 12 months ---
            try {
                // Get the first booking date for the customer
                ResponseEntity<BookingPageView> firstBookingResponse = getRestTemplate().getForEntity(
                        "http://localhost:8080/getFirstBookingDate?consumerId=" + booking.getConsumerId(),
                        BookingPageView.class);
                if (firstBookingResponse.getStatusCode().is2xxSuccessful() && firstBookingResponse.getBody() != null) {
                    LocalDate firstBookingDate = firstBookingResponse.getBody().getBookingDate();
                    LocalDate twelveMonthsLater = firstBookingDate.plusMonths(12);

                    // Only check surcharge if today's date is within the 12-month window
                    if (!LocalDate.now().isAfter(twelveMonthsLater)) {
                        ResponseEntity<Integer> countResponse = getRestTemplate().getForEntity(
                                "http://localhost:8080/getBookingCountInLast12Months?consumerId=" + booking.getConsumerId(),
                                Integer.class);
                        int count = countResponse.getBody();
                        // If 6 or more bookings already exist, then this booking is the 7th (or higher)
                        if (count >= 6) {
                            surcharge = (long) (price * 0.2);
                            model.addAttribute("surchargeMessage", "You have exceeded 6 bookings within the last 12 months. A 20% surcharge has been added.");
                        }
                    }
                }
            } catch (Exception ex) {
                // If there is an error retrieving the booking history, assume no surcharge applies.
            }

            // --- Fetch available cylinder ID ---
            String cylinderId = getRestTemplate().getForEntity(
                    "http://localhost:8080/getCylinderId?type=" + booking.getConnType(),
                    String.class).getBody();
            System.out.println(cylinderId);
            if (cylinderId == null) {
                model.addAttribute("sorry", "Sorry, Cylinder not available");
                return "booking";
            }

            // --- Proceed with booking ---
            booking.setCylinderid(cylinderId);
            getRestTemplate().postForEntity(
                    "http://localhost:8080/updateCylinder?cylinderid=" + cylinderId,
                    booking,
                    String.class);
            booking.setBookingDate(LocalDate.now());

            // Save booking with customer & cylinder ID
            getRestTemplate().postForEntity(
                    "http://localhost:8080/addbooking?consumerId=" + booking.getConsumerId() + "&cylinderid=" + cylinderId,
                    booking,
                    BookingPageView.class);

            // Fetch recent booking for confirmation display
            ResponseEntity<BookingPageView> recentResponse = getRestTemplate().getForEntity(
                    "http://localhost:8080/recentBooking",
                    BookingPageView.class);
            if (recentResponse.getStatusCode().is2xxSuccessful() && recentResponse.getBody() != null) {
                model.addAttribute("recent", recentResponse.getBody());
            }

            long totalPrice = price + surcharge; // Final price after surcharge
            booking.setSurcharge(surcharge);
            booking.setPrice(totalPrice);

            // Pass data to success page
            model.addAttribute("book", booking);
            model.addAttribute("price", totalPrice);
            model.addAttribute("surcharge", surcharge);

            return "bsuccess";  // Redirects to bsuccess.html where the popup will be handled
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to submit booking: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/downloadBill")
    public ResponseEntity<byte[]> downloadBill(@ModelAttribute BookingPageView bill) {
        try {
            ResponseEntity<BookingPageView[]> response = getRestTemplate().getForEntity(
                    "http://localhost:8080/customer?consumerId=" + bill.getConsumerId(), BookingPageView[].class);

            ResponseEntity<BookingPageView[]> billResponse = getRestTemplate().getForEntity(
                    "http://localhost:8080/bill?bookingId=" + bill.getBookingId(), BookingPageView[].class);

            if (!response.getStatusCode().is2xxSuccessful() || !billResponse.getStatusCode().is2xxSuccessful()) {
                throw new Exception("API call failed with non-2xx response.");
            }

            BookingPageView customerDetails = Optional.ofNullable(response.getBody())
                    .filter(body -> body.length > 0)
                    .map(body -> body[0])
                    .orElseThrow(() -> new Exception("No customer details found."));

            BookingPageView billDetails = Optional.ofNullable(billResponse.getBody())
                    .filter(body -> body.length > 0)
                    .map(body -> body[0])
                    .orElseThrow(() -> new Exception("No bill details found."));

            long gst = 10;
            long deliveryCharge = "Express".equalsIgnoreCase(billDetails.getDeliveryOption()) ? 100 : 50;
            long basePrice = 1000;
            long surcharge = Optional.ofNullable(billDetails.getSurcharge()).orElse(0L);
            long totalPrice = basePrice + gst + deliveryCharge + surcharge;

            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // Font Definitions
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Font thankYouFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC, BaseColor.BLUE);
            Font redfont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC, BaseColor.RED);


            // Header
            Paragraph title = new Paragraph("Booking Confirmation Bill", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(15);
            title.setSpacingAfter(15);
            document.add(title);

            Paragraph deliveryDate = new Paragraph("Delivery Date: " + billDetails.getDeliveryDate(), headingFont);
            deliveryDate.setAlignment(Element.ALIGN_CENTER);
            deliveryDate.setSpacingBefore(5);
            document.add(deliveryDate);

            // Booking Details
            Paragraph bookingDetailsParagraph = new Paragraph("Booking Details", headingFont);
            bookingDetailsParagraph.setAlignment(Element.ALIGN_LEFT);
            bookingDetailsParagraph.setSpacingBefore(10);
            document.add(bookingDetailsParagraph);

            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(80);
            detailsTable.setSpacingBefore(10);
            detailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            addTableRow(detailsTable, "Booking ID:", String.valueOf(billDetails.getBookingId()), labelFont, valueFont);
            addTableRow(detailsTable, "Customer Name:", customerDetails.getConsumerName(), labelFont, valueFont);
            addTableRow(detailsTable, "Email:", customerDetails.getEmail(), labelFont, valueFont);
            addTableRow(detailsTable, "Phone Number:", String.valueOf(customerDetails.getContactNo()), labelFont, valueFont);
            addTableRow(detailsTable, "Connection Type:", customerDetails.getConnType(), labelFont, valueFont);
            addTableRow(detailsTable, "Time Slot:", billDetails.getTimeSlot(), labelFont, valueFont);
            addTableRow(detailsTable, "Delivery Option:", billDetails.getDeliveryOption(), labelFont, valueFont);
            addTableRow(detailsTable, "Payment Option:", billDetails.getPaymentOption(), labelFont, valueFont);
            document.add(detailsTable);

            // Price Details
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<BookingPageView> responses = restTemplate.getForEntity("http://localhost:8080/bills?billId="+ + bill.getBookingId(), BookingPageView.class);
            BookingPageView billResponses = responses.getBody();

            document.add(new Paragraph("\n")); // Line break
            PdfPTable billTable = new PdfPTable(2);
            billTable.setWidthPercentage(50);
            billTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addTableRow(billTable, "Base Price:", "₹ " + basePrice, labelFont, valueFont);
            addTableRow(billTable, "GST:", "₹ " + gst, redfont, valueFont);
            addTableRow(billTable, "Delivery Charge:", "₹ " + deliveryCharge, labelFont, valueFont);

                addTableRow(billTable, "Surcharge (20% extra):", "₹ " + billResponses.getSurcharge(), labelFont, valueFont);

            addTableRow(billTable, "Total Price:", "₹ " + billResponses.getTotalPrice(), redfont, valueFont);
            document.add(billTable);

            // Thank You
            Paragraph thankYou = new Paragraph("Thank you for booking with us!", thankYouFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            thankYou.setSpacingBefore(20);
            document.add(thankYou);

            document.close();

            byte[] pdfBytes = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=bill.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating bill: " + e.getMessage()).getBytes());
        }
    }

    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

   



    @GetMapping("/history")
    public String bookingHistory(@RequestParam String consumerId, Model model) {
        try {
            ResponseEntity<BookingPageView[]> response = getRestTemplate().getForEntity(
                    "http://localhost:8080/history?consumerId=" + consumerId, BookingPageView[].class);
//            ResponseEntity<BookingPageView> recentResponse = getRestTemplate().getForEntity(
//                    "http://localhost:8080/recentBooking", BookingPageView.class);
//
//            if (recentResponse.getStatusCode().is2xxSuccessful() && recentResponse.getBody() != null) {
//                model.addAttribute("recent", recentResponse.getBody());
//            }

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                model.addAttribute("history", response.getBody());
                model.addAttribute("consumerId" ,consumerId);
            } else {
                model.addAttribute("error", "No history available for this consumer.");
            }

            return "history";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to fetch booking history: " + e.getMessage());
            return "error";
        }
    }

    @Bean
    private RestOperations getRestTemplate() {
        return new RestTemplate();
    }
}
