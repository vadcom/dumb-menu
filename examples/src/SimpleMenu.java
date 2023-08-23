import link.sigma5.menu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;

public class SimpleMenu extends Frame implements MenuListener, KeyListener {

    private final DumbMenu menu = new DumbMenu("SimpleMenu.yaml", this);
    ;
    private final Font font = new Font("DIALOG", Font.BOLD, 26);
    private final JLabel optionsLabel;

    public SimpleMenu(String title) throws HeadlessException, IOException {
        super(title);
        setBounds(50, 50, 500, 400);
        addKeyListener(this);
        setVisible(true);
        add(this.optionsLabel = new JLabel());
        this.optionsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        this.optionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.optionsLabel.setBackground(Color.gray);
        this.optionsLabel.setForeground(Color.orange);
        this.optionsLabel.setOpaque(true);
        menu.init();
        repaint();
    }

    void setOption(String option) {
        optionsLabel.setText("Option is " + option);
    }

    @Override
    public void onEvent(MenuEvent event) {
        switch (event.event()) {
            case "exit" -> dispose();
            case "Option" -> setOption(event.param());
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(font);
        menu.drawMenu(level -> {
            int y = 100;
            for (MenuItemView menuItemView : level.getItemsText()) {
                g.setColor(menuItemView.active() ? Color.MAGENTA : Color.BLUE);
                String text = menuItemView.value().isEmpty() ? menuItemView.text() : menuItemView.text() + " : " + menuItemView.value();
                g.drawString(text, 100, y);
                y += 50;
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_ENTER -> menu.performAction(MenuAction.Enter);
            case VK_UP -> menu.performAction(MenuAction.Previous);
            case VK_DOWN -> menu.performAction(MenuAction.Next);
            case VK_ESCAPE -> menu.performAction(MenuAction.Back);
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) throws IOException {
        new SimpleMenu("AWT menu sample");
    }

}
