
const API = "http://localhost:8080/api";
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "index.html";
}

const headers = {
    "Authorization": "Bearer " + token,
    "Content-Type": "application/json"
};

// 1. Load recipes for selection
async function loadRecipes() {
    try {
        const response = await fetch(`${API}/recipes`, {
            headers
        });

        if (!response.ok) throw new Error("Unable to load recipes");

        const recipes = await response.json();
        const container = document.getElementById("recipeSelection");

        if (recipes.length === 0) {
            container.textContent = "Add some recipes first.";
            return;
        }

        container.innerHTML = "";

        recipes.forEach(recipe => {
            const label = document.createElement("label");
            const checkbox = document.createElement("input");

            checkbox.type = "checkbox";
            checkbox.className = "recipe-checkbox";
            checkbox.value = recipe.id;

            label.appendChild(checkbox);
            label.appendChild(
                document.createTextNode(" " + recipe.title)
            );

            container.appendChild(label);
            container.appendChild(document.createElement("br"));
        });
    } catch (error) {
        document.getElementById("recipeSelection").textContent =
            error.message;
    }
}

// 2. Generate shopping list
async function generateShoppingList() {
    const recipeIds = Array.from(
        document.querySelectorAll(".recipe-checkbox:checked")
    ).map(checkbox => Number(checkbox.value));

    if (recipeIds.length === 0) {
        alert("Please select at least one recipe.");
        return;
    }

    try {
        const response = await fetch(
            `${API}/shopping-list/generate`,
            {
                method: "POST",
                headers,
                body: JSON.stringify(recipeIds)
            }
        );

        if (!response.ok) {
            throw new Error(await response.text());
        }

        alert("Shopping list generated successfully!");
        await loadShoppingList();

    } catch (error) {
        alert(error.message);
    }
}

// 3. Display shopping list
async function loadShoppingList() {
    const container = document.getElementById("shoppingList");

    try {
        const response = await fetch(`${API}/shopping-list`, {
            headers
        });

        if (!response.ok) {
            throw new Error("Unable to load shopping list");
        }

        const items = await response.json();
        container.innerHTML = "";

        if (items.length === 0) {
            container.textContent = "Your shopping list is empty.";
            return;
        }

        items.forEach(item => {
            const row = document.createElement("div");
            const checkbox = document.createElement("input");
            const name = document.createElement("span");
            const deleteButton = document.createElement("button");

            checkbox.type = "checkbox";
            checkbox.checked = item.purchased;

            checkbox.addEventListener("change", () => {
                updatePurchased(item.id, checkbox.checked);
            });

            name.textContent = " " + item.item + " ";

            if (item.purchased) {
                name.style.textDecoration = "line-through";
            }

            deleteButton.textContent = "Delete";
            deleteButton.addEventListener("click", () => {
                deleteItem(item.id);
            });

            row.append(checkbox, name, deleteButton);
            container.appendChild(row);
        });

    } catch (error) {
        container.textContent = error.message;
    }
}

// 4. Mark an item as purchased
async function updatePurchased(id, purchased) {
    try {
        const response = await fetch(
            `${API}/shopping-list/${id}/purchased`,
            {
                method: "PUT",
                headers,
                body: JSON.stringify({ purchased })
            }
        );

        if (!response.ok) {
            throw new Error("Unable to update item");
        }

        await loadShoppingList();
    } catch (error) {
        alert(error.message);
        await loadShoppingList();
    }
}

// 5. Delete an item
async function deleteItem(id) {
    if (!confirm("Delete this item?")) return;

    try {
        const response = await fetch(
            `${API}/shopping-list/${id}`,
            {
                method: "DELETE",
                headers
            }
        );

        if (!response.ok) {
            throw new Error("Unable to delete item");
        }

        await loadShoppingList();
    } catch (error) {
        alert(error.message);
    }
}

// 6. Button events
document.getElementById("generateBtn")
    .addEventListener("click", generateShoppingList);

document.getElementById("logoutBtn")
    .addEventListener("click", () => {
        localStorage.removeItem("token");
        window.location.href = "index.html";
    });

loadRecipes();
loadShoppingList();
