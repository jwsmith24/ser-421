"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Module1Activity4A_1 = require("./Module1Activity4A");
// test cases from assignment doc:
(0, Module1Activity4A_1.calc)('{ "operation": "add", "operand": 5 }');
(0, Module1Activity4A_1.calc)('{ "operation": "subtract", "operand": 3 }');
(0, Module1Activity4A_1.calc)('{ "operation": "add", "operand": 19 }');
// failing cases
(0, Module1Activity4A_1.calc)('{ "operation": "add", "operand": hi }'); // bad operand
(0, Module1Activity4A_1.calc)('{ "operation": "not real", "operand": 87 }'); // bad operation
(0, Module1Activity4A_1.calc)('{ "something": "add", "anotherThing": 20 }'); // bad props
(0, Module1Activity4A_1.calc)("not a json string"); // invalid json string
