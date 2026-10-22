// This file calculates the bonus for a salesman based on their sales amount.
const { calculateBonus } = require("./salesman");

const sales = 50000;
const bonus = calculateBonus(sales);

console.log("Sales:", sales);
console.log("Bonus:", bonus);