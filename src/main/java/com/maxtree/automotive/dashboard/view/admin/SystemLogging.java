package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            String item = null;
            if(e.getFirstSelectedItem().isPresent()) {
                item = e.getFirstSelectedItem().get();
                readLogFile(item);
            }
        });
        com.vaadin.contextmenu.ContextMenu menu2 = new com.vaadin.contextmenu.ContextMenu(tree, true);
        menu2.addItem("导出文件", new com.vaadin.contextmenu.Menu.Command() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
                Set<String> set = tree.getSelectedItems();
                List<String> list = new ArrayList<String>(set);
                String selectedFile = list.get(0);
                sendConvertedFileToUser(selectedFile);
            }
        });

        return tree;
    }

    private void sendConvertedFileToUser(final String filename) {
        // 在本地下载并打开
        UI ui = UI.getCurrent();
        Resource resource = new FileResource(new File("logs/"+filename));
        ui.getPage().open(resource, "log/"+filename, false);
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
