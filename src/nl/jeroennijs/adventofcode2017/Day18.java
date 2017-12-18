package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class Day18 {

    private static Pattern instrPattern = Pattern.compile("(snd|set|add|mul|mod|rcv|jgz) +(-?[a-z0-9]+) *(-?[a-z0-9]+)?");

    public static void main(String[] args) throws IOException {
        final List<Instruction> instructions = parseLines("day18.txt");

        long lastSound = execute(new ExecutionState(0L), instructions, null, null);
        System.out.println("Result of run 1:" + lastSound); // 3423

        long valuesSent1 = executePart2(instructions);
        System.out.println("Result of run 2:" + valuesSent1); // 7493
    }

    private static long executePart2(List<Instruction> instructions) {
        final List<Long> sendQueue = new LinkedList<>();
        final List<Long> rcvQueue = new LinkedList<>();
        final ExecutionState executionState0 = new ExecutionState(0L);
        final ExecutionState executionState1 = new ExecutionState(1L);
        long result0 = 0L;
        long result1 = 0L;
        do {
            result0 = execute(executionState0, instructions, sendQueue, rcvQueue);
            result1 = execute(executionState1, instructions, rcvQueue, sendQueue);
        } while (result0 != -1 || result1 != -1 || !sendQueue.isEmpty() || !rcvQueue.isEmpty());

        return executionState1.getValuesSent();
    }

    private static List<Instruction> parseLines(String fileName) throws IOException {
        return Utils.readLines(fileName).stream()
            .map(Day18::parseLine)
            .collect(Collectors.toList());
    }

    private static Instruction parseLine(String line) {
        final Matcher matcher = instrPattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Error in line: " + line);
        }
        return new Instruction(matcher.group(1), matcher.group(2), matcher.group(3));
    }

    private static long execute(ExecutionState executionState, List<Instruction> instructions, List<Long> sendQueue, List<Long> rcvQueue) {
        long sound = 0;
        while (executionState.getPc() < instructions.size()) {
            Instruction instruction = instructions.get(executionState.getPc());
            switch (instruction.opCode) {
                case set:
                    executionState.setRegister(instruction.operand1.register, executionState.getValue(instruction.operand2));
                    executionState.incPc();
                    break;
                case add:
                    executionState.setRegister(instruction.operand1.register, executionState.getValue(instruction.operand1) + executionState.getValue(instruction.operand2));
                    executionState.incPc();
                    break;
                case mul:
                    executionState.setRegister(instruction.operand1.register, executionState.getValue(instruction.operand1) * executionState.getValue(instruction.operand2));
                    executionState.incPc();
                    break;
                case mod:
                    executionState.setRegister(instruction.operand1.register, executionState.getValue(instruction.operand1) % executionState.getValue(instruction.operand2));
                    executionState.incPc();
                    break;
                case snd:
                    if (sendQueue == null) {
                        sound = executionState.getValue(instruction.operand1);
                    } else {
                        sendQueue.add(executionState.getValue(instruction.operand1));
                        executionState.valueSent();
                    }
                    executionState.incPc();
                    break;
                case rcv:
                    if (rcvQueue == null) {
                        if (sound != 0) {
                            return sound;
                        }
                    } else {
                        if (rcvQueue.isEmpty()) {
                            return -1;
                        }
                        Long value = rcvQueue.remove(0);
                        executionState.setRegister(instruction.operand1.register, value);
                    }
                    executionState.incPc();
                    break;
                case jgz:
                    if (executionState.getValue(instruction.operand1) > 0) {
                        executionState.incPc(executionState.getValue(instruction.operand2));
                    } else {
                        executionState.incPc();
                    }
                    break;
            }
        }
        return 0;
    }

    private static void print(Map<String, Long> registers) {
        for (String register : registers.keySet()) {
            System.out.print(register + ":" + registers.get(register) + " ");
        }
        System.out.println();
    }

    public static class Instruction {
        public enum OpCode {
            snd, set, add, mul, mod, rcv, jgz
        }

        Instruction(String opCode, String operand1, String operand2) {
            this.opCode = OpCode.valueOf(opCode);
            this.operand1 = Operand.fromValue(operand1);
            if (operand2 != null) {
                this.operand2 = Operand.fromValue(operand2);
            }
        }

        OpCode opCode;
        Operand operand1;
        Operand operand2;

        @Override public String toString() {
            return opCode + " " + operand1 + (operand2 != null ? " " + operand2 : "");
        }
    }



    public static class Operand {
        String register;
        int value;

        static Operand fromValue(String value) {
            return isAlpha(value) ? new Operand(value) : new Operand(Integer.valueOf(value));
        }

        private Operand(String register) {
            this.register = register;
        }

        private Operand(Integer value) {
            this.value = value;
        }

        private static boolean isAlpha(String value) {
            for (char c : value.toCharArray()) {
                if (c < 'a' || c > 'z')
                    return false;
            }
            return true;
        }

        boolean isRegister() {
            return register != null;
        }

        @Override public String toString() {
            return isRegister() ? register : String.valueOf(value);
        }
    }



    public static class ExecutionState {
        private int pc = 0;
        private long valuesSent = 0;
        Map<String, Long> registers = new TreeMap<>();

        ExecutionState(long programId) {
            registers.put("p", programId);
        }

        int getPc() {
            return this.pc;
        }

        long getValuesSent() {
            return valuesSent;
        }

        void incPc() {
            incPc(1);
        }

        void incPc(long steps) {
            this.pc += steps;
        }

        void valueSent() {
            this.valuesSent++;
        }

        long getValue(Operand operand) {
            return operand.isRegister() ? getRegister(operand.register) : operand.value;
        }

        private long getRegister(String register) {
            if (!registers.containsKey(register)) {
                registers.put(register, 0L);
            }
            return registers.get(register);
        }

        void setRegister(String register, Long value) {
            registers.put(register, value);
        }
    }
}
