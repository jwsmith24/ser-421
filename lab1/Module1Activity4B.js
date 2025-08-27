"use strict";
var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.Calculator = void 0;
var Calculator = /** @class */ (function () {
    function Calculator(initialValue) {
        this.result = initialValue;
        this.history = [initialValue]; // result stack
    }
    Calculator.prototype.calc = function (jsonString) {
        // parse the provided json string, handle invalid input
        try {
            var expression = JSON.parse(jsonString); //throws syntax error if it doesn't match the type
            if (expression.operation === 'add' && typeof (expression.operand) == "number") {
                this.result += expression.operand;
            }
            else if (expression.operation === 'subtract' && typeof (expression.operand) == "number") {
                this.result -= expression.operand;
            }
            else {
                throw new Error("Invalid JSON string provided | operation: ".concat(expression.operation, " operand: ").concat(expression.operand));
            }
            console.log("got: ", expression, this.result);
        }
        catch (exception) {
            if (exception instanceof Error) {
                console.error(exception.message);
            }
        }
        this.history = __spreadArray([this.result], this.history, true); // add latest result first, then spread in the existing results
        return this.result;
    };
    // Leverages the stack
    Calculator.prototype.undo = function () {
        if (this.history.length <= 1) {
            console.log("Removing the last result in the stack.. resetting..");
            this.result = 0;
            this.history = [0];
            return this.result;
        }
        else {
            this.pop();
            this.result = this.history[0]; // update result to previous result
            return this.result;
        }
    };
    Calculator.prototype.peek = function (n) {
        if (this.history.length === 0) {
            return null;
        }
        if (n === undefined) {
            // no args provided, return the last result
            return this.history[0];
        }
        // check for valid index
        if (n < 0 || n > this.history.length) {
            return null;
        }
        return this.history[n];
    };
    Calculator.prototype.pop = function () {
        if (this.history.length === 0) {
            return null;
        }
        return this.history.splice(0, 1)[0]; // splice the first index of the array and then return the number
    };
    Calculator.prototype.printMe = function () {
        var message = this.history.length > 0 ? "Stack Contents: " + this.history : "The stack is empty";
        console.log(message);
    };
    Calculator.prototype.clear = function () {
        // reset the stack
        this.history = [0];
        console.log("Results cleared");
    };
    return Calculator;
}());
exports.Calculator = Calculator;
