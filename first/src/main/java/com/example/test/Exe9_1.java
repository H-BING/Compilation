package com.example.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by 59800 on 2018/1/6.
 */

public class Exe9_1 extends JFrame{

    private JTextField no = new JTextField();
    private JTextField name = new JTextField();
    private JTextField key = new JTextField();
    private JTextField keyCheck = new JTextField();
    private JTextField[] textFields = new JTextField[4];
    private JComboBox comboBox = new JComboBox();
    private JCheckBox[] checkBoxes = new JCheckBox[4];

    public static void main(String[] args) {
        Exe9_1 e = new Exe9_1();
    }

    public Exe9_1() {
        super("学生注册");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(7, 1));
        setContent(jPanel);

        setResizable(false);
        setSize(400, 700);
        add(jPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setContent(JPanel jPanel) {
//        String strs[] = {"学号", "姓名", "密码", "再输入一次密码"};
//        for (int i = 0; i < 4; i++) {
//            addNormalContent(jPanel, textFields[i], strs[i]);
//        }

        addNormalContent(jPanel, no, "学号");
        addNormalContent(jPanel, name, "姓名");
        addNormalContent(jPanel, key, "密码");
        addNormalContent(jPanel, keyCheck, "再输入一次密码");

        addMajor(jPanel);
        addGrade(jPanel);
        addButton(jPanel);
    }

    public void addButton(final JPanel jPanel) {
        JButton button = new JButton("注册");
        button.setSize(100, 50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Component component = null;
                if (!checkNo(no.getText())) {
                    JOptionPane.showMessageDialog(component, "学号格式不对");
                } else if (!checkName(name.getText())) {
                    JOptionPane.showMessageDialog(component, "姓名不超过6个汉字");
                } else if (key.getText().length() < 6 ||
                        key.getText().length() > 8) {
                    JOptionPane.showMessageDialog(component, "密码不少于6个字符，不多于8个字符");
                } else if (!key.getText().equals(keyCheck.getText())) {
                    JOptionPane.showMessageDialog(component, "2次输入的密码必须完全相同");
                } else if (!checkGrade(checkBoxes)) {
                    JOptionPane.showMessageDialog(component, "年级只能选择一项");
                } else {

                    JOptionPane.showMessageDialog(component, "学号：" + no.getText() +
                            "\n姓名：" + name.getText() +
                            "\n密码：" + key.getText() +
                            "\n再次输入密码：" + keyCheck.getText());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button);
        jPanel.add(buttonPanel);
    }

    boolean checkNo(String str) {
        if (str.length() > 6)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    boolean checkName(String str) {
        if (str.length() > 6)
            return false;
//        for (int i = 0; i < str.length(); i++) {
//            if (!Character.isDigit(str.charAt(i))) {
//                return false;
//            }
//        }
        return true;
    }

    boolean checkGrade(JCheckBox[] checkBoxes) {
        int count = 0;
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                count++;
            }
        }
        if (count > 1 || count == 0)
            return false;
        return true;
    }

    public void addNormalContent(JPanel jPanel, JTextField textField, String hint) {
        JPanel content = new JPanel();
        content.setLayout(new GridLayout(1, 2));
        content.add(new JLabel(hint));
        content.add(textField);
        jPanel.add(content);
    }

    public void addMajor(JPanel jPanel) {
        JPanel major = new JPanel();
        major.setLayout(new GridLayout(1, 2));
        major.add(new JLabel("专业"));

        JComboBox comboBox = new JComboBox();
        String[] strs = {"数学", "计算机", "外语"};
        for (int i = 0; i < strs.length; i++) {
            comboBox.addItem(strs[i]);
        }
        major.add(comboBox);
        jPanel.add(major);
    }

    public void addGrade(JPanel jPanel) {
        JPanel grade = new JPanel();
        grade.setLayout(new GridLayout(1, 2));
        grade.add(new JLabel("年级"));

        String[] strs = {"一年级", "二年级", "三年级", "四年级"};
        JPanel grades = new JPanel();
        grades.setLayout(new GridLayout(2, 2));
        for (int i = 0; i < strs.length; i++) {
            checkBoxes[i] = new JCheckBox(strs[i]);
            grades.add(checkBoxes[i]);
        }
        grade.add(grades);
        jPanel.add(grade);
    }
}
