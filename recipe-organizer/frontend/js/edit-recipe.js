const token = localStorage.getItem("token");
const recipeId = localStorage.getItem("editRecipeId");

if (!token) {
    window.location.href = "index.html";
}

if (!recipeId) {
    alert("No recipe selected.");
    window.location.href = "recipes.html";
}


// ===============================
// LOAD RECIPE
// ===============================

async function loadRecipe() {

    try {

        const response = await fetch(
            `http://localhost:8080/api/recipes/${recipeId}`,
            {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Recipe not found");
        }

        const recipe = await response.json();

        document.getElementById("title").value =
            recipe.title || "";

        document.getElementById("category").value =
            recipe.category || "";

        document.getElementById("description").value =
            recipe.description || "";

        document.getElementById("ingredients").value =
            recipe.ingredients || "";

        document.getElementById("instructions").value =
            recipe.instructions || "";

    } catch (error) {

        console.error(error);

        document.getElementById("message").textContent =
            "Unable to load recipe.";
    }
}


// ===============================
// UPDATE RECIPE
// ===============================

document.getElementById("editRecipeForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const updatedRecipe = {

            title: document.getElementById("title").value,

            category: document.getElementById("category").value,

            description:
                document.getElementById("description").value,

            ingredients:
                document.getElementById("ingredients").value,

            instructions:
                document.getElementById("instructions").value
        };

        try {

            const response = await fetch(
                `http://localhost:8080/api/recipes/${recipeId}`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + token
                    },

                    body: JSON.stringify(updatedRecipe)
                }
            );

            if (!response.ok) {

                const errorText = await response.text();

                throw new Error(errorText);
            }

            document.getElementById("message").textContent =
                "Recipe updated successfully!";

            // Remove stored ID
            localStorage.removeItem("editRecipeId");

            // Go back to recipes after a short delay
            setTimeout(() => {
                window.location.href = "recipes.html";
            }, 1000);

        } catch (error) {

            console.error(error);

            document.getElementById("message").textContent =
                "Failed to update recipe.";
        }
    });


// Load the selected recipe
loadRecipe();