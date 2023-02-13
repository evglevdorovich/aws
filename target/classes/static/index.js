
function sendProductToShoppingCart(product) {
    console.log(product)
    fetch("/shoppingCarts/products", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(product)
    })
        .then(r => console.log(r))
        .catch(e => console.error(e))
}

async function sendUser(form) {
    let formData = new FormData(form);
    let user = {};
    formData.forEach(function(value, key){
        user[key] = value;
    });
    let resp = await fetch("/users/registration", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(user)
    })
    let respJson = await resp.json();
    let error = respJson.errorMessage;
    if (error != null) {
        document.getElementById("errorMessage").innerText = error;
    }
    else {
        window.location.href = "http://localhost:8080/login"
    }
}