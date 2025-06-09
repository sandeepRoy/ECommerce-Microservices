Thanks for sharing your `index.html`. The issue is clear now: your frontend **is not including the `Authorization` header** in the fetch call to `/load-cart`, which is required now that the endpoint is secured.

---

### ✅ Fix: Add `Authorization` Header to the Fetch Call

You'll need to:

1. Retrieve the `access_token` from storage (e.g., `localStorage` or `sessionStorage`).
2. Add it to the `fetch()` call for `/load-cart`.

---

### 🛠️ Updated JavaScript Block in Your `index.html`

Replace this part of your code:

```javascript
fetch(endpointUrl)
    .then(response => response.json())
    .then(data => {
        // ...
    })
    .catch(error => {
        console.error("Error fetching data:", error);
    });
```

with:

```javascript
const token = localStorage.getItem("access_token"); // Or wherever you're storing it

fetch(endpointUrl, {
    method: 'GET',
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    }
})
.then(response => {
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
})
.then(data => {
    // Populate the form fields
    document.getElementById("customer_name").value = data.customer_name;
    document.getElementById("customer_email").value = data.customer_email;
    document.getElementById("customer_mobile").value = data.customer_mobile;
    document.getElementById("total_amount").value = data.total_amount;

    const address = `${data.delivery_address.address}, ${data.delivery_address.city}, ${data.delivery_address.state}, ${data.delivery_address.pincode}`;
    document.getElementById("delivery_address").value = address;

    const wishlistElement = document.getElementById('wishlist');
    wishlistElement.innerHTML = '';
    data.wishlist.forEach(item => {
        const li = document.createElement('li');
        li.textContent = `${item.product_name} (Qty: ${item.product_quantity}) - ₹${item.payable_amount}`;
        wishlistElement.appendChild(li);
    });
})
.catch(error => {
    console.error("Error fetching data:", error);
});
```

---

### 🔐 Where to Store the Token

* If you're already saving the access token after login in `localStorage` or `sessionStorage`, this will work.
* If not, you need to **store the token after successful login** at the Customer side like so:

```javascript
// Example: Save token after login
localStorage.setItem("access_token", response.access_token);
```

---

### ✅ Summary

* You need to modify the JavaScript in your HTML page to send the `Authorization` header.
* Make sure the token is available in the browser (usually after logging in).
* Your backend will now receive the token as expected and serve the secured `/load-cart` endpoint.

Let me know if you’d like help updating other parts like token storage or Razorpay integration!
