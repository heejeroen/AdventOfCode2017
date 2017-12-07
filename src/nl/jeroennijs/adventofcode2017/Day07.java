package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.jeroennijs.adventofcode2017.Utils.readLines;



public class Day07 {

    private static final Pattern PATTERN = Pattern.compile("([a-z]+) \\(([0-9]+)\\)( -> )?([a-z ,]+)?");
    private static final Comparator<ProgramTreeNode> TOTAL_WEIGHT_COMPARATOR = Comparator.comparingInt(ProgramTreeNode::getTotalWeight);

    public static void main(String[] args) throws IOException {
        ProgramTreeNode root = ProgramTreeParser.readTree("day07.txt");
        System.out.println("Bottom node: " + root.getName()); // gynfwly

        final ProgramTreeNode unbalancedChild = root.findUnbalancedChild();
        System.out.println("Corrected weight: " + unbalancedChild.getCorrectedWeight()); // 1526
    }

    static class ProgramTreeParser {
        private static List<ProgramTreeNode> nodes;

        static ProgramTreeNode readTree(String fileName) throws IOException {
            nodes = new ArrayList<>();
            for (String line : readLines(fileName)) {
                parseLine(line);
            }
            return nodes.get(0).getRoot();
        }

        private static void parseLine(String line) {
            final Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                ProgramTreeNode currentNode = findOrCreateNode(matcher.group(1), Integer.valueOf(matcher.group(2)));

                List<String> childrenNames = (matcher.groupCount() > 2 && matcher.group(4) != null)
                    ? Arrays.asList(matcher.group(4).split(", "))
                    : Collections.emptyList();
                findOrCreateChildren(currentNode, childrenNames);
            }
        }

        private static ProgramTreeNode findOrCreateNode(String name, Integer weight) {
            ProgramTreeNode node = findOrCreateNode(name);
            node.setWeight(weight);
            return node;
        }

        private static ProgramTreeNode findOrCreateNode(String name) {
            ProgramTreeNode existingNode = findNode(name);
            if (existingNode != null) {
                return existingNode;
            }
            final ProgramTreeNode newNode = new ProgramTreeNode(name);
            nodes.add(newNode);
            return newNode;
        }

        private static ProgramTreeNode findNode(String name) {
            return nodes.stream()
                .filter(node -> node.getName().equals(name))
                .findFirst()
                .orElse(null);
        }

        private static void findOrCreateChildren(ProgramTreeNode currentNode, List<String> childrenNames) {
            for (String childrenName : childrenNames) {
                final ProgramTreeNode child = findOrCreateNode(childrenName);
                child.setParentNode(currentNode);
                currentNode.getChildren().add(child);
            }
        }
    }

    static class ProgramTreeNode {
        private final String name;
        private ProgramTreeNode parentNode;
        private final List<ProgramTreeNode> children = new ArrayList<>();
        private int weight;
        private int totalWeight;

        ProgramTreeNode(String name) {
            this.name = name;
        }

        void setParentNode(ProgramTreeNode parentNode) {
            this.parentNode = parentNode;
        }

        List<ProgramTreeNode> getChildren() {
            return children;
        }

        String getName() {
            return name;
        }

        int getWeight() {
            return weight;
        }

        void setWeight(int weight) {
            this.weight = weight;
        }

        ProgramTreeNode getRoot() {
            return parentNode == null ? this : parentNode.getRoot();
        }

        int getTotalWeight() {
            if (this.totalWeight != 0) {
                return totalWeight;
            }
            this.totalWeight = weight + children.stream().mapToInt(ProgramTreeNode::getTotalWeight).sum();
            return totalWeight;
        }

        ProgramTreeNode findUnbalancedChild() {
            final Optional<ProgramTreeNode> maxNode = children.stream().max(TOTAL_WEIGHT_COMPARATOR);
            int maxWeight = maxNode.map(ProgramTreeNode::getTotalWeight).orElse(0);
            int minWeight = children.stream().min(TOTAL_WEIGHT_COMPARATOR).map(ProgramTreeNode::getTotalWeight).orElse(0);
            if (maxWeight == minWeight) {
                return parentNode;
            }
            return maxNode.get().findUnbalancedChild();
        }

        int getCorrectedWeight() {
            final Optional<ProgramTreeNode> maxNode = children.stream().max(TOTAL_WEIGHT_COMPARATOR);
            int maxWeight = maxNode.map(ProgramTreeNode::getTotalWeight).orElse(0);
            int minWeight = children.stream().min(TOTAL_WEIGHT_COMPARATOR).map(ProgramTreeNode::getTotalWeight).orElse(0);
            return maxNode.map(n -> n.getWeight() - (maxWeight - minWeight)).orElse(0);
        }
    }
}
