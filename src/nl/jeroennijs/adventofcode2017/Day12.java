package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Day12 {

    public static void main(String[] args) throws IOException {
        final Map<String, ProgramNode> nodes = Parser.readGraph("day12.txt");
        final ProgramNode nodeZero = nodes.get("0");
        System.out.println("Node 0 is in a group with " + NodeCounter.countNodesFrom(nodeZero, new HashSet<>()) + " nodes"); // 283
        System.out.println("There are " + NodeCounter.countGroups(nodes.values()) + " groups found"); // 195
    }

    private static class Parser {
        private static final Pattern PATTERN = Pattern.compile("([0-9]+)( <-> )(.*)");

        static Map<String, ProgramNode> readGraph(String fileName) throws IOException {
            final Map<String, ProgramNode> nodes = new HashMap<>();
            Utils.readLines(fileName).stream()
                .map(PATTERN::matcher)
                .filter(Matcher::matches)
                .forEach(matcher -> connectNodes(nodes, matcher.group(1), matcher.group(3).split(", ")));

            return nodes;
        }

        private static void connectNodes(Map<String, ProgramNode> nodes, String currentNodeId, String[] childrenNodeIds) {
            final ProgramNode currentNode = findOrCreateNode(currentNodeId, nodes);
            for (String childrenNodeId : childrenNodeIds) {
                final ProgramNode childNode = findOrCreateNode(childrenNodeId, nodes);
                currentNode.addChildIfAbsent(childNode);
                childNode.addParentIfAbsent(currentNode);
            }
        }

        private static ProgramNode findOrCreateNode(String nodeId, Map<String, ProgramNode> nodes) {
            return nodes.computeIfAbsent(nodeId, ProgramNode::new);
        }
    }

    private static class NodeCounter {

        static int countNodesFrom(ProgramNode startNode, Set<ProgramNode> nodesFound) {
            if (nodesFound.contains(startNode)) {
                return 0;
            }
            nodesFound.add(startNode);
            return 1 + countNodes(startNode.getChildren(), nodesFound) + countNodes(startNode.getParents(), nodesFound);
        }

        static int countGroups(Collection<ProgramNode> nodes) {
            final Set<ProgramNode> nodesCounted = new HashSet<>();
            int nrOfGroups = 0;
            for (ProgramNode node : nodes) {
                if (!nodesCounted.contains(node)) {
                    final Set<ProgramNode> nodesInGroup = new HashSet<>();
                    countNodesFrom(node, nodesInGroup);
                    nodesCounted.addAll(nodesInGroup);
                    nrOfGroups++;
                }
            }
            return nrOfGroups;
        }

        private static int countNodes(List<ProgramNode> nodes, Set<ProgramNode> nodesFound) {
            return nodes.stream()
                .filter(node -> !nodesFound.contains(node))
                .mapToInt(node -> countNodesFrom(node, nodesFound))
                .sum();
        }
    }

    private static class ProgramNode {
        private String id;
        private List<ProgramNode> children = new ArrayList<>();
        private List<ProgramNode> parents = new ArrayList<>();

        ProgramNode(String id) {
            this.id = id;
        }

        String getId() {
            return id;
        }

        List<ProgramNode> getChildren() {
            return children;
        }

        void addChildIfAbsent(ProgramNode child) {
            if (children.stream().noneMatch(n -> n.getId().equals(child.getId()))) {
                children.add(child);
            }
        }
        List<ProgramNode> getParents() {
            return parents;
        }

        void addParentIfAbsent(ProgramNode parent) {
            if (parents.stream().noneMatch(n -> n.getId().equals(parent.getId()))) {
                parents.add(parent);
            }
        }
    }
}
