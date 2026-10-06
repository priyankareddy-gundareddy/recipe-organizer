const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "index.html";
}

document.getElementById("recipeForm").addEventListener("submit", async function (event) {

    event.preventDefault();

    const recipe = {
        title: document.getElementById("title").value,
        category: document.getElementById("category").value,
        description: document.getElementById("description").value,
        ingredients: document.getElementById("ingredients").value,
        instructions: document.getElementById("instructions").value
    };

    try {

        const response = await fetch(
            "https://recipe-organizer-pi1n.onrender.com/api/recipes",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },

                body: JSON.stringify(recipe)
            }
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Failed to add recipe");
        }

        document.getElementById("message").textContent =
            "Recipe added successfully!";

        document.getElementById("recipeForm").reset();

    } catch (error) {

        console.error(error);

        document.getElementById("message").textContent =
            error.message;
    }
});