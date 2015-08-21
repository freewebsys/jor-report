package jatools.designer.variable.dialog;

import jatools.designer.App;
import jatools.dom.src.XmlNodeSource;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class XmlSourceDialog extends JDialog implements ActionListener{
    private JTextField tagName;
    private JTextField url;
    private JTextField xpath;
    private static XmlNodeSource source;
    private static boolean exitedOK;
    private Component c;
    public XmlSourceDialog(Component c) {
      super((Frame)javax.swing.SwingUtilities.getWindowAncestor(c));
        this.c=c;
        exitedOK=false;
        this.setTitle(App.messages.getString("res.281"));
        this.setModal(true);
        this.setSize(270,160);
        this.setLocation(250,250);
        initUI();
    }
    private void initUI(){
        JPanel center=new JPanel();

        JLabel label=new JLabel(App.messages.getString("res.269"));
        tagName=new JTextField();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        center.setLayout(new GridBagLayout());

        center.add(label, gbc); //
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 70;
        center.add(tagName, gbc);
        gbc.weightx = 0;

        label=new JLabel(App.messages.getString("res.282"));
        url=new JTextField();
        gbc.gridwidth = 1;
        center.add(label, gbc); //
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        center.add(url, gbc);

        label=new JLabel(App.messages.getString("res.283"));
        xpath=new JTextField();
        gbc.gridwidth = 1;
        center.add(label, gbc); //
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        center.add(xpath, gbc);


        JButton ok=new JButton(App.messages.getString("res.3"));
        JButton cancel=new JButton(App.messages.getString("res.4"));
        ok.setPreferredSize(new Dimension(78,23));
        cancel.setPreferredSize(new Dimension(78,23));
        ok.setActionCommand("ok");
        cancel.setActionCommand("cancel");
        ok.addActionListener(this);
        cancel.addActionListener(this);
        Box south=Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(ok);
        south.add(Box.createHorizontalStrut(10));
        south.add(cancel);
        south.add(Box.createHorizontalStrut(10));

        center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        south.setBorder(BorderFactory.createEmptyBorder(10,5,5,0));

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(center,BorderLayout.CENTER);
        this.getContentPane().add(south,BorderLayout.SOUTH);
    }
    private void setSource(XmlNodeSource source){
        this.source=source;
        if(source!=null){
            tagName.setText(source.getTagName());
            url.setText(source.getUrl());
            xpath.setText(source.getXPath());
        }
    }
    public static XmlNodeSource getXmlNodeSource(XmlNodeSource _source,Component c){
        XmlSourceDialog dialog=new XmlSourceDialog(c);
        dialog.setSource(_source);
        dialog.setVisible(true);
        if(exitedOK)
        return source;
    else
        return null;
    }


    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("ok")){
            if(checkNull()){
                return;
            }
            if(source==null){
                source=new XmlNodeSource(tagName.getText(),url.getText(),xpath.getText());
            }
            else{
                source.setTagName(tagName.getText());
                source.setUrl(url.getText());
                source.setXPath(xpath.getText());
            }
            exitedOK=true;
            this.dispose();
        }
        else if(e.getActionCommand().equals("cancel")){
            exitedOK=false;
            this.dispose();
        }
    }
    private boolean checkNull(){
         boolean b=false;
        if(tagName.getText()==null||tagName.getText().trim().equals("")){
            JOptionPane.showConfirmDialog(c,App.messages.getString("res.284"),App.messages.getString("res.82"),
               JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
            b=true;
        }
        else  if(url.getText()==null||url.getText().trim().equals("")){
            JOptionPane.showConfirmDialog(c,App.messages.getString("res.285"),App.messages.getString("res.82"),
               JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
            b=true;
        }
        return b;
    }
}
