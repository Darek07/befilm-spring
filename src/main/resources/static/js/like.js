function changeClass(inputElement) {
    var labelElement = inputElement.parentNode;

    if (labelElement.classList.contains("img-like2")) {
        labelElement.classList.remove("img-like2");
        labelElement.classList.add("img-liked2");
    } else if (labelElement.classList.contains("img-liked2")) {
        labelElement.classList.remove("img-liked2");
        labelElement.classList.add("img-like2");
    }
}