package model;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            root = new TreeNode().setName(instance.getName()).setisFile(instance.isFile());
            parents = root;
        } else {
            parents = p;
        }
        for(File subs : instance.listFiles()) {
            if(subs.isFile()) {
                parents.addToSub(new TreeNode().setName(subs.getName()).setisFile(true).setSize(subs.length()/1024).setExtention(subs.getName().split("\\.")[1]));
                //System.out.println(subs.getName() + " file has extention " + parents.getSubInstanceIn(parents.getSubSize()-1).getExtention());
            } else {
                TreeNode split = new TreeNode().setName(subs.getName()).setisFile(false);
                //System.out.println(subs.getName() + " folder is added to " + parents.getFileName());
                parents.addToSub(split);
                BuildStructure(subs.toString(), split);
            }
        }
    }

    private void printStructure(TreeNode tn) {
        System.out.println("I am " + tn.getFileName() + " I have: ");
        for(int i = 0; i < tn.getSubSize(); i++) {
            System.out.println("     + " + tn.getSubInstanceIn(i).getFileName());
        }
        for(int i = 0; i < tn.getSubSize(); i++) {
            if(!tn.getIsFile()) {
                printStructure(tn.getSubInstanceIn(i));
            }
        }
    }

//    BuildStructure("res/", -1);
//    private void BuildStructure(String path, int i) {
//        File instance = new File(path);
//        if(instance.isFile()) {
//            structure.add(new TreeNode().setName(instance.getName()).setisFile(true).setParentsIndex(i).setSize(instance.length()/1024));
//        } else {
//            structure.add(new TreeNode().setName(instance.getName()).setisFile(false).setParentsIndex(i));
//            int p_ind = structure.size() - 1;
//            for(File file : instance.listFiles()){
//                BuildStructure(file.toString(), p_ind);
//            }
//        }
//    }

    public TreeNode getRoot() {
        return this.root;
    }
}
