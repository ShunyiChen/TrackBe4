package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class SystemLogging extends HorizontalSplitPanel {

    private Label secondComponent = new Label("", ContentMode.HTML);

    public SystemLogging() {
        initComponents();
    }

    private void initComponents() {
        setSizeFull();
        setSplitPosition(300, Unit.PIXELS);
        Tree firstComponent = leftComponent();

        secondComponent.setWidth(100, Unit.PERCENTAGE);

        setFirstComponent(firstComponent);
        setSecondComponent(secondComponent);
    }

    private Tree leftComponent() {
        Tree<String> tree = new Tree<>("日志文件");
        TreeData<String> data = new TreeData<>();
        File[] logFiles = new File("logs").listFiles();
        for(File f : logFiles) {
            data.addItem(null,f.getName());
        }

        tree.setDataProvider(new TreeDataProvider<>(data));
//        tree.expand("Desktops", "Laptops");
        tree.setItemIconGenerator(item -> {
            return VaadinIcons.FILE;
        });
        tree.addSelectionListener(e ->{
            readLogFile( e.getFirstSelectedItem().get());
        });
        return tree;
    }

    private void readLogFile(String fileName) {
        StringBuilder str = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("logs/"+fileName)),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                str.append(lineTxt);
                str.append("<br>");
            }
            br.close();
            secondComponent.setValue(str.toString());
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }
}
