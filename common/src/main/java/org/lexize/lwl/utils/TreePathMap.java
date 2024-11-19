package org.lexize.lwl.utils;

import java.util.HashMap;

public class TreePathMap<E> {
    private final TreeNode<E> rootNode;

    public TreePathMap(E rootElement) {
        rootNode = new TreeNode<>(rootElement);
    }

    public TreePathMap() {
        this(null);
    }

    private static class TreeNode<E> {
        private E element;
        private HashMap<String, TreeNode<E>> children = new HashMap<>();
        private TreeNode(E rootElement) {

        }
        private TreeNode() {
            this(null);
        }
    }

    public void add(String path, E value) {
        String[] components = path.split("/");
        TreeNode<E> currentNode = rootNode;
        for (String elem: components) {
            currentNode = currentNode.children.computeIfAbsent(elem, k -> new TreeNode<>());
        }
        currentNode.element = value;
    }

    public E get(String path) {
        String[] components = path.split("/");
        return get(rootNode, components, 0);
    }

    public E getExact(String path) {
        String[] components = path.split("/");
        TreeNode<E> currentNode = rootNode;
        for (String elem: components) {
            TreeNode<E> node = currentNode.children.get(elem);
            if (node == null) return null;
            currentNode = node;
        }
        return currentNode.element;
    }

    private static <E> E get(TreeNode<E> rootNode, String[] path, int currentPathIndex) {
        if (currentPathIndex >= path.length) return rootNode.element;
        String elem = path[currentPathIndex];
        TreeNode<E> node = rootNode.children.get(elem);
        if (node != null) {
            E value = get(node, path, currentPathIndex + 1);
            return value != null ? value : rootNode.element;
        }
        return rootNode.element;
    }
}
