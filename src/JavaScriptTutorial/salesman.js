//Exports a function that calculates the bonus for a salesman based on their sales.
function calculateBonus(sales) {
    if (sales < 0) {
        throw new Error("Sales cannot be negative");
    }

    return sales * 0.05;
}
// Exports the calculateBonus function so it can be used in other files.
module.exports = { calculateBonus };