document.addEventListener("DOMContentLoaded", function () {
    // Show alert message if available
    var alertMessage = document.getElementById("alertMessage");
    if (alertMessage && alertMessage.innerText.trim() !== "") {
        alert(alertMessage.innerText);
    }

    var alertMessages = document.getElementById("alertMessages");
    if (alertMessages && alertMessages.innerText.trim() !== "") {
        alert(alertMessages.innerText);
    }
});

// Function to check the booking date
function checkDate() {
    const today = new Date(); // Get today's date
    today.setHours(0, 0, 0, 0); // Set time to midnight for accurate comparison

    const bookingDateInput = document.getElementById("check");
    const bookingDate = new Date(bookingDateInput.value);

    if (bookingDate < today) {
        alert("Booking date must be today or in the future.");
        bookingDateInput.value = ""; // Clear invalid date
    }
}
