package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private File file;
    private List<TreeNode> subStructure;
    private String extension;

    public TreeNode() {
        subStructure = new ArrayList<>();
    }

    public TreeNode setFile(File file) {
        this.file = file;
        return this;
    }

    public TreeNode setExtension(String s) {
        extension = s;
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

    public File getFile() {
        return this.file;
    }

    public String getExtension() {
        return this.extension;
    }

    public double getFileSize() {
        if (file.isFile()) {
            return this.file.length();
        } else {
            double fs = 0;
            for(int i = 0; i < subStructure.size(); i++) {
                fs += subStructure.get(i).getFileSize();
            }
            return fs;
        }
    }
}
