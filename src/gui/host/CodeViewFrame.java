package gui.host;

import client.ClientConnection;
import function.Procedure;
import gui.Const;
import gui.components.CustomButton;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * A separate frame used to view submitted client programs.
 * @author Harry Xu
 * @version 1.0 - January 13th 2024
 */
class CodeViewFrame extends JFrame {
    private final ClientConnection client;
    private final Procedure onDelete;

    /**
     * Constructs a {@link CodeViewFrame}.
     * @param client the client connection which holds the code
     * @param onDelete a callback function called when the host attempts
     *                 to delete the client's submission.
     */
    public CodeViewFrame(ClientConnection client, Procedure onDelete) {
        this.client = client;
        this.onDelete = onDelete;

        // Frame setup
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(Const.FRAME_WIDTH, Const.FRAME_HEIGHT));

        // Component set up
        JLabel nameView = new JLabel();
        JTextArea codeView = new JTextArea();
        CustomButton deleteSubmission = new CustomButton("Delete Submission");
        deleteSubmission.addActionListener(new DeleteSubmissionHandler());

        nameView.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, 24));
        nameView.setText(this.client.getName());
        nameView.setHorizontalAlignment(SwingConstants.CENTER);
        nameView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        codeView.setText(this.client.getCode());
        codeView.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
        codeView.setLineWrap(true);
        codeView.setTabSize(2);
        codeView.setWrapStyleWord(true);
        codeView.setColumns(20);
        codeView.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(codeView);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Adding components to frame
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(nameView, BorderLayout.PAGE_START);
        this.add(deleteSubmission, BorderLayout.PAGE_END);
    }

    /**
     * Handles the action event when the host attempts to delete a program submission via the GUI.
     * Calls the {@link CodeViewFrame#onDelete} callback and closes the frame.
     * @author Harry Xu
     * @version 1.0 - January 13th 2024
     */
    private class DeleteSubmissionHandler implements ActionListener {
        /**
         * actionPerformed
         * Called when the delete button is pressed.
         * @param e the {@link ActionEvent} object.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            client.deleteCode();

            onDelete.execute();

            // Close window
            dispatchEvent(new WindowEvent(CodeViewFrame.this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
