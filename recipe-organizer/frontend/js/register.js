const registerForm = document.getElementById("registerForm");
const message = document.getElementById("message");

registerForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {

        const response = await fetch(
            "http://localhost:8080/api/auth/register",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );

        const data = await response.json();

        if (response.ok) {

            message.textContent =
                "Registration successful!";

            message.style.color = "green";

            setTimeout(() => {
                window.location.href = "index.html";
            }, 1000);

        } else {

            message.textContent =
                data.error || "Registration failed";

            message.style.color = "red";
        }

    } catch (error) {

        message.textContent =
            "Cannot connect to the backend.";

        message.style.color = "red";

        console.error(error);
    }
});