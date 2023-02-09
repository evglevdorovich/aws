
function sendProductToShoppingCart(product) {

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
        const suffix = ':8080/login';
        window.location.href = '//' + window.location.hostname + suffix;
    }
}

async function sendShippingAddress(form) {
    let formData = new FormData(form);
    let shippingAddress;

    formData.forEach(function(value){
        shippingAddress = value;
    });

    await fetch("/shoppingCarts/shippingAddresses", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(shippingAddress)
    })

    const suffix = ':8080/shoppingCarts';
    window.location.href = '//' + window.location.hostname + suffix;
}

async function changeAmountOfProducts(inputId, productId) {
    let input = document.getElementById(inputId);
    console.log(input.value, productId)
    const product = {
        productId: productId,
        changedAmount: input.value
    }
    await fetch("/warehouse", {
        method: "PATCH",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(product)
    });

    const suffix = ':8080/warehouse';
    window.location.href = '//' + window.location.hostname + suffix;
}