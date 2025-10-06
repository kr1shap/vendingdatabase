# 🍿 Vending Machine DB

## ⁉️ Project Overview

This project simulates a **smart vending machine** that functions like a mini-grocery store. Customers can select snacks and drinks, pay via cash or credit card, and receive their items. The vending machine also enforces **stock limits** and **expiry dates**, ensuring that only available and safe items can be purchased.

What makes this vending machine unique is its dual perspective functionality: it operates differently for **customers** and **re-stockers**, providing tailored features for each user type.

Fully built in Java, this was one of my first, older projects!

---

## ‼️ Features

### Customer Features

* **Item Selection & Purchase**: Customers can browse and purchase snacks and drinks, with automatic stock and expiry verification.
* **Pricing & Payment**: The machine calculates the total price and accepts payments via cash or credit card.
* **Discount Codes**: Customers can enter special discount codes to reduce the price.
* **Ratings & Reviews**: Customers can rate purchased items; these ratings are visible to future users.
* **Loyalty Program**: Users can sign up for free, earn points for purchases, and redeem them for future discounts.
* **Dietary Preferences**: Customers can input dietary restrictions, and the machine customizes the menu for future visits.

### Re-Stocker Features

* **Stock Management**: View current stock levels and expiry dates of items.
* **Re-Stocking**: Add new items or replenish existing stock.
* **Expiry Updates**: Input expiry dates for new or restocked items to ensure customers can only access valid products.

---

## System Workflow

1. **Customer Interaction**

           Rate previous purchase → select item(s) → view price → enter discount code (optional) → pay (cash/credit) → get the item! 

For loyalty members, they can input their dietary restrictions so the menu is modified according to their needs.

2. **Re-Stocker Interaction**

                           view stock levels and expiry dates → re-stock items → update expiry dates.  

3. **Loyalty & Personalization**

   * Loyalty points are tracked per user.
   * Dietary restrictions are stored and used to personalize the menu on future visits.

---

## 💫 Future Enhancements

* Web-based interface for remote interaction !!

---
