const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {

        const response = await fetch(
            "https://recipe-organizer-pi1n.onrender.com/api/auth/login",
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

            localStorage.setItem("token", data.token);

            message.textContent = "Login successful!";
            message.style.color = "green";

            // Dashboard will be created in the next step
            setTimeout(() => {
                window.location.href = "dashboard.html";
            }, 1000);

        } else {

            message.textContent =
                data.error || "Invalid email or password";

            message.style.color = "red";
        }

    } catch (error) {

        message.textContent =
            "Cannot connect to the backend.";

        message.style.color = "red";

        console.error(error);
    }
});