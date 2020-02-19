package model;

import java.io.File;

public class TreeMap {
    private static TreeMap treeMap;
    private TreeNode root;

    private TreeMap() {
        BuildStructure("res/", null);

    }

    public static TreeMap getInstance() {
        if(treeMap == null) {
            treeMap = new TreeMap();
        }
        return treeMap;
    }

    private void BuildStructure(String path, TreeNode p) {
        File instance = new File(path);
        TreeNode parents;
        if(p == null) {
            root = new TreeNode().setFile(instance);
            parents = root;
        } else {
            parents = p;
        }
        for(File subs : instance.listFiles()) {
            if(subs.isFile()) {
                String[] nsp; // name split
                parents.addToSub(new TreeNode().setFile(subs).setExtension(((nsp = subs.getName().split("\\.")).length == 2)?nsp[1]:"none"));
                //System.out.println(subs.getName() + " file has extension " + parents.getSubInstanceIn(parents.getSubSize()-1).getExtention());
            } else {
                TreeNode split = new TreeNode().setFile(subs);
                //System.out.println(subs.getName() + " folder is added to " + parents.getFileName());
                parents.addToSub(split);
                BuildStructure(subs.toString(), split);
            }
        }
    }

    public void resetRootDir(String path) {
        BuildStructure(path, null);
    }

    public TreeNode getRoot() {
        return this.root;
    }
}
