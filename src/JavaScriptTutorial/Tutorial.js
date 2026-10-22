//Object Creation:
//Create an Object literal that contains a set of properties
const salesman = {
    firstName: "Max",
    lastName: "Mustermann",
    sales: 50000
};

//Create an Object using the Object constructor
const person = new Object({
    firstName: "Max",
    lastName: "Mustermann",
    sales: 50000
});
//Is basically the same Object literal, but with a different syntax


//Functions and Callbacks:
//Create a function that can be used to reuse code and perform a specific task
function calculateBonus(sales) {
    const bonus = sales * 0.5;
    return bonus;
}

//Create a function that takes another function as an argument (callback)
function processSales(sales, callback) {
    const bonus = sales * 0.05;
    callback(bonus);
}

function showBonus(bonus) {
    console.log("Bonus: " + bonus + " Euro");
}

//In this case showBonus is passed as a callback function to processSales, which will be called after the bonus is calculated
processSales(50000, showBonus);

//Pyramid of Doom is a term used to describe the situation where multiple nested callbacks are used, making the code hard to read and maintain
getSales(function (sales) {
    calculateBonus(sales, function (bonus) {
        saveBonus(bonus, function (result) {
            sendNotification(result, function () {
                console.log("Process completed");
            });
        });
    });
});


//Promises:
//A Promise is an object that represents the final result of an asynchronous operation. It can be in one of three states: pending, fulfilled, or rejected.
//Create a Promise
const bonusPromise = new Promise((resolve, reject) => {
    const sales = 50000;

    if (sales >= 0) {
        const bonus = sales * 0.05;
        resolve(bonus);
    } else {
        reject(new Error("Sales cannot be negative"));
    }
});

//Here we use the .then() method to handle the fulfilled state of the Promise
bonusPromise
    .then(bonus => {
        console.log("Bonus: " + bonus + " Euro");
    })
    //Here we use the .catch() method to handle the rejected state of the Promise as an error handling
    .catch(error => {
        console.error("Error: " + error.message);
    });


//Async/Await:
//Async/Await are Keywords built on top of Promises that allows us to write asynchronous code in a more synchronous manner.
//ASync functions always return a Promise, and the Await keyword can be used to wait for the Promise to resolve or reject.
//Create an async function that uses the await keyword to wait for the Promise to resolve
async function main() {
    try {
        const bonus = await calculateBonus(50000);
        console.log("Bonus: " + bonus + " Euro");
    } catch (error) {
        console.error("Error: " + error.message);
    }
}


//REST-based Interfacce with Express.js:
//Express.js is a web framework for Node.js that allows us to create REST-based interfaces.
const express = require("express");
//Create an Express application
const app = express();
const PORT = 3000;
//Create a GET endpoint that returns an object as JSON
app.get("/salesman", (req, res) => {
    res.json({
        firstName: "Max",
        lastName: "Mustermann",
        sales: 50000
    });
});
//Start the server and listen on the specified port
app.listen(PORT, () => {
    console.log(`Server is running at http://localhost:${PORT}`);
});


//Consuming Rest-based Interfaces with Axios:
//Axios is a library that allows applications to make HTTP requests to REST-based interfaces and handle the responses.
const axios = require("axios");
//Create an async function that uses Axios to make a GET request
async function getSalesman() {
    try {
        //Make a GET request to the endpoint
        const response = await axios.get(
            "http://localhost:3000/salesman"
        );
        //Contains the response data from the endpoint
        console.log(response.data);
    } catch (error) {
        console.error("Error: " + error.message);
    }
}


//Handling Cookies in Express.js:
//Cookies are small pieces of data that are stored on the client-side and sent to the server with each request. They can be used to store user preferences, session information, and other data.
const express = require("express");
const cookieParser = require("cookie-parser");
//Create an Express application
const app = express();
//Use the cookie-parser to parse cookies from the request headers
app.use(cookieParser());
//Create a GET endpoint that sets a cookie
app.get("/set-cookie", (req, res) => {
    //Set a cookie with the name "username" and value "Max"
    res.cookie("username", "Max", { httpOnly: true });
    //Send a response to the client
    res.send("Cookie created");
});
//Create a GET endpoint that reads the cookie
app.get("/read-cookie", (req, res) => {
    const username = req.cookies.username;
    res.send(username || "Cookie not found");
});
//Create a GET endpoint that deletes the cookie
app.get("/delete-cookie", (req, res) => {
    //Delete the cookie with the name "username"
    res.clearCookie("username");
    res.send("Cookie deleted");
});


//Module for implementing the calculation of the bonus
//salesman.js exports the function and makes it available to other files with module.export
//bonus.js uses the function to calculate the bonus


//Observer Pattern Implemantation using RxJS
//Observer Pattern ist design Pattern that observes and notifies subscribed Objects when new Information is published
// Import the Subject class from RxJS
const { Subject } = require("rxjs");

// Create the subject
const salesSubject = new Subject();

// Observer 1: Calculate the bonus
salesSubject.subscribe(sales => {
    console.log("Bonus:", sales * 0.05);
});

// Observer 2: Display the sales amount
salesSubject.subscribe(sales => {
    console.log("Sales received:", sales);
});

// Publish a new sales value
salesSubject.next(50000);


//Term definitions
// Asynchrony: A task can start and finish later without blocking other work.

// Concurrency: Multiple tasks make progress during overlapping periods. They do not have to run at exactly the same time.

// Parallelism: Multiple tasks execute at the same time, for example on different CPU cores.

