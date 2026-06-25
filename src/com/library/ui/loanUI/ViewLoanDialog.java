package com.library.ui.loanUI;

import com.library.conroller.LoanBookController;
import com.library.domain.enums.LoanStatus;
import com.library.services.DTOs.LoanBookDto;
import com.library.services.DTOs.LoanBookListDTO;
import com.library.services.PenaltyCalculationResult;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import java.awt.*;

public class ViewLoanDialog extends JDialog {
    //-----
    private LoanBookController loanBookController;
    //-----
    private LoanBookListDTO loan;
    //button
    private JButton btnReturn, btnCancel;

    public ViewLoanDialog(Frame parent ,LoanBookController loanBookController, int loanId)
    {
        super(parent, "Loan " + loanId + " Details", true);
        this.loanBookController = loanBookController;
        //---------------------------
        setLayout(new BorderLayout(10,10));
        setResizable(false);

        this.loan = loanBookController.handleGetLoanById(loanId);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderCreator.createBorder("Loan Info"));
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.VERTICAL;

        //----------------
        //loan id
        gbc.gridx=0;gbc.gridy=0;
        formPanel.add(new JLabel("Loan ID : " + loan.getLoanId()), gbc);

        //member name
        gbc.gridx=0;gbc.gridy=1;
        formPanel.add(new JLabel("Member Name : " + loan.getMemberName()), gbc);

        //book title
        gbc.gridx=0;gbc.gridy=2;
        formPanel.add(new JLabel("Book Title : " + loan.getTitle()), gbc);

        //expire date
        gbc.gridx=0;gbc.gridy=3;
        formPanel.add(new JLabel("Expire Date : " + loan.getToDate()), gbc);

        //status
        gbc.gridx=0;gbc.gridy=4;
        formPanel.add(new JLabel("Status : " + loan.getStatus()), gbc);

        //late days & amount
        if(!loan.getStatus().equals(LoanStatus.RETURNED)){
            PenaltyCalculationResult penalty = loanBookController.handleCalculatePenalty(loan.getToDate());
            gbc.gridx=0;gbc.gridy=5;
            formPanel.add(new JLabel("Late Days : " + penalty.getLateDays()), gbc);
            gbc.gridx=0;gbc.gridy=6;
            formPanel.add(new JLabel("Penalty Cost : " + penalty.getPenaltyCost() + "$"), gbc);
        }


        add(formPanel, BorderLayout.CENTER);

        //---------------
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btnReturn = new JButton("Return Book");
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());


        if(!loan.getStatus().equals(LoanStatus.RETURNED)){
            btnReturn.addActionListener(e -> returnBook());
            buttonPanel.add(btnReturn);
        }
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    //return book
    private void returnBook(){
        try {
            boolean isDone = loanBookController.handleReturnBook(
                    new LoanBookDto(loan.getLoanId(), loan.getMemberId(), loan.getBookId(),
                            loan.getFromDate(),
                            loan.getToDate(), loan.getStatus())
            );
            if(isDone){
                JOptionPane.showMessageDialog(null,
                        "Book Returned Successfully!");
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "Book Returned Failed!");
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
