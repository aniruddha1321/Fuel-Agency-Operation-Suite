document.addEventListener("DOMContentLoaded", function () {
    var message = document.querySelector("h2"); // Success message element
    if (message && message.innerText.trim() !== "") {
        alert(message.innerText); // Show success message as an alert
    }
    
    // Optional: Redirect after 5 seconds
    setTimeout(function () {
        window.location.href = "/home"; // Change to your desired page
    }, 5000);
});
