package org.figuramc.fwl.utils;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

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

    public void add(ResourceLocation location, E value) {
        add(location.getPath(), value);
    }

    public void add(String path, E value) {
        String[] components = path.split("/");
        add(List.of(components).iterator(), value);
    }

    public void add(Iterator<String> path, E value) {
        TreeNode<E> currentNode = rootNode;
        while (path.hasNext()) {
            String elem = path.next();
            currentNode = currentNode.children.computeIfAbsent(elem, k -> new TreeNode<>());
        }
        currentNode.element = value;
    }

    public void add(E value, String... paths) {
        ArrayList<String> pathList = new ArrayList<>();
        for (String path: paths) {
            Collections.addAll(pathList, path.split("/"));
        }
        add(pathList.iterator(), value);
    }

    public E get(String path) {
        List<String> components = List.of(path.split("/"));
        return get(rootNode, components.iterator());
    }

    public E get(String... paths) {
        ArrayList<String> pathList = new ArrayList<>();
        for (String path: paths) {
            Collections.addAll(pathList, path.split("/"));
        }
        return get(rootNode, pathList.iterator());
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

    private static <E> E get(TreeNode<E> rootNode, Iterator<String> path) {
        if (!path.hasNext()) return rootNode.element;
        String elem = path.next();
        TreeNode<E> node = rootNode.children.get(elem);
        if (node != null) {
            E value = get(node, path);
            return value != null ? value : rootNode.element;
        }
        return rootNode.element;
    }
}
