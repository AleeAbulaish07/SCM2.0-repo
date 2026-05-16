console.log("Script loaded");

let currentTheme = getTheme();

// initial
changeTheme();

function changeTheme() {
    const html = document.querySelector("html");

    // reset and apply current theme
    html.classList.remove("light", "dark");
    html.classList.add(currentTheme);

    const changeThemeButton = document.querySelector("#theme_change_button");

    if (!changeThemeButton) return;

    changeThemeButton.querySelector("span").textContent =
        currentTheme === "light" ? "dark" : "light";

    changeThemeButton.addEventListener("click", () => {
        const oldTheme = currentTheme;

        console.log("change theme button clicked");

        // toggle theme
        currentTheme = currentTheme === "dark" ? "light" : "dark";

        // save
        setTheme(currentTheme);

        // ✅ FIX: old theme remove karo
        html.classList.remove(oldTheme);

        // new theme add karo
        html.classList.add(currentTheme);

        // update button text
        changeThemeButton.querySelector("span").textContent =
            currentTheme === "light" ? "dark" : "light";
    });
}

// localStorage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

function getTheme() {
    return localStorage.getItem("theme") || "light";
}