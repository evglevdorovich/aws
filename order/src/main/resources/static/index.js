function sendProductToShoppingCart(product) {

    fetch("/shoppingCarts/products", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(product)
    })
        .then(r => console.log(r))
        .catch(e => console.error(e))
}

function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0,
            v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
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

async function changeAmountOfProducts(productId) {
    let input = document.getElementById(productId);
    const suffixToPatch = `:8080/warehouse`;
    console.log(input.value, productId);
    const productQuantity = parseInt(input.value);
    let orderId = generateUUID();
    if (productQuantity === 0) {
        window.location.href = '//' + window.location.hostname + suffixToPatch;
        return;
    }

    else if(productQuantity > 0) {
        orderId += '_deposit'
    }
    else {
        orderId += '_withdrawal'
    }
    const product = {
        orderId: orderId,
        quantity: Math.abs(productQuantity)
    }
    await fetch(`/products/${productId}`, {
        method: "PATCH",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(product)
    });

    window.location.href = '//' + window.location.hostname + suffixToPatch;
}