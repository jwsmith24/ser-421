"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.calc = calc;
// start with 0
var result = 0;
function calc(jsonString) {
    // parse the provided json string, handle invalid input
    try {
        var expression = JSON.parse(jsonString); //throws syntax error if it doesn't match the type
        if (expression.operation === 'add' && typeof (expression.operand) == "number") {
            result += expression.operand;
        }
        else if (expression.operation === 'subtract' && typeof (expression.operand) == "number") {
            result -= expression.operand;
        }
        else {
            throw new Error("Invalid JSON string provided | operation: ".concat(expression.operation, " operand: ").concat(expression.operand));
        }
        console.log("got: ", expression, result);
    }
    catch (exception) {
        if (exception instanceof Error) {
            console.error(exception.message);
        }
    }
    return result;
}
/*TEST CASES*/
// test cases from assignment doc:
calc('{ "operation": "add", "operand": 5 }');
calc('{ "operation": "subtract", "operand": 3 }');
calc('{ "operation": "add", "operand": 19 }');
// failing cases
calc('{ "operation": "add", "operand": hi }'); // bad operand
calc('{ "operation": "not real", "operand": 87 }'); // bad operation
calc('{ "something": "add", "anotherThing": 20 }'); // bad props
calc("not a json string"); // invalid json string
