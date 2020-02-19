package model;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String fileName;
    private double fileSize;
    private List<TreeNode> subStructure;
    private boolean isFile;
    private String extention;

    public TreeNode() {
        subStructure = new ArrayList<>();
    }

    public TreeNode setName(String name) {
        this.fileName = name;
        return this;
    }

    public TreeNode setSize(double size) {
        this.fileSize = size;
        return this;
    }

    public TreeNode setisFile(boolean b) {
        this.isFile = b;
        return this;
    }

    public TreeNode setExtention(String s) {
        extention = s;
        return this;
    }

    public int getSubSize() {
        return subStructure.size();
    }

    public TreeNode getSubInstanceIn(int i) {
        return subStructure.get(i);
    }

    public void addToSub(TreeNode tn) {
        subStructure.add(tn);
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean getIsFile() {
        return this.isFile;
    }

    public String getExtention() {
        return this.extention;
    }

    public double getFileSize() {
        if (isFile) {
            return this.fileSize;
        } else {
            double fs = 0;
            for(int i = 0; i < subStructure.size(); i++) {
                fs += subStructure.get(i).getFileSize();
            }
            return fs;
        }
    }
}
