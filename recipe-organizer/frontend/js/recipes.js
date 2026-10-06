const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "index.html";
}


// ===============================
// LOAD ALL RECIPES
// ===============================

async function loadRecipes() {

    try {

        const response = await fetch(
            "http://localhost:8080/api/recipes",
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load recipes");
        }

        const recipes = await response.json();

        displayRecipes(recipes);

    } catch (error) {

        console.error(error);

        document.getElementById("recipeContainer").innerHTML =
            "<p>Unable to load recipes. Please check the backend.</p>";
    }
}


// ===============================
// LOAD RECIPES BY CATEGORY
// ===============================

async function loadRecipesByCategory(category) {

    try {

        const response = await fetch(
            `http://localhost:8080/api/recipes/category/${encodeURIComponent(category)}`,
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load recipes by category");
        }

        const recipes = await response.json();

        displayRecipes(recipes);

    } catch (error) {

        console.error(error);

        document.getElementById("recipeContainer").innerHTML =
            "<p>Unable to load recipes for this category.</p>";
    }
}


// ===============================
// DISPLAY RECIPES
// ===============================

function displayRecipes(recipes) {

    const container =
        document.getElementById("recipeContainer");


    if (recipes.length === 0) {

        container.innerHTML =
            "<p>No recipes found in this category.</p>";

        return;
    }


    container.innerHTML = "";


    recipes.forEach(recipe => {

        const card = document.createElement("div");

        card.className = "recipe-card";


        card.innerHTML = `

            <h3>${recipe.title}</h3>

            <p>
                <strong>Category:</strong>
                ${recipe.category}
            </p>

            <p>
                <strong>Description:</strong>
                ${recipe.description}
            </p>

            <p>
                <strong>Ingredients:</strong>
                ${recipe.ingredients}
            </p>

            <p>
                <strong>Instructions:</strong>
                ${recipe.instructions}
            </p>

            <button onclick="editRecipe(${recipe.id})">
                Edit Recipe
            </button>

            <button onclick="deleteRecipe(${recipe.id})">
                Delete Recipe
            </button>

        `;


        container.appendChild(card);

    });
}


// ===============================
// CATEGORY FILTER
// ===============================

document
    .getElementById("categoryFilter")
    .addEventListener("change", function () {

        const selectedCategory = this.value;


        if (selectedCategory === "all") {

            loadRecipes();

        } else {

            loadRecipesByCategory(selectedCategory);

        }

    });


// ===============================
// EDIT RECIPE
// ===============================

function editRecipe(id) {

    localStorage.setItem(
        "editRecipeId",
        id
    );

    window.location.href =
        "edit-recipe.html";
}


// ===============================
// DELETE RECIPE
// ===============================

async function deleteRecipe(id) {

    const confirmDelete =
        confirm(
            "Are you sure you want to delete this recipe?"
        );


    if (!confirmDelete) {
        return;
    }


    try {

        const response = await fetch(

            `http://localhost:8080/api/recipes/${id}`,

            {
                method: "DELETE",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        if (!response.ok) {

            throw new Error(
                "Failed to delete recipe"
            );
        }


        alert(
            "Recipe deleted successfully!"
        );


        loadRecipes();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to delete recipe."
        );
    }
}


// ===============================
// LOGOUT
// ===============================

document
    .getElementById("logoutBtn")
    .addEventListener(
        "click",
        function () {

            localStorage.removeItem(
                "token"
            );

            window.location.href =
                "index.html";

        }
    );


// ===============================
// LOAD RECIPES WHEN PAGE OPENS
// ===============================

loadRecipes();