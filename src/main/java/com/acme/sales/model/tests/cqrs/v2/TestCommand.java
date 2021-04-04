package com.acme.sales.model.tests.cqrs.v2;

import com.acme.sales.model.Proposal;
import com.acme.sales.model.VacationPackage;
import com.acme.sales.model.cqrs.CommandException;
import com.acme.sales.model.cqrs.v2.command.CreateProposalCommand;

import java.util.ArrayList;
import java.util.Date;

public class TestCommand {

    public static void main(String[] args){

        // 1. Create a Vacation Package for testing
        VacationPackage vacationPackage_1 = new VacationPackage("BAHAMA5NIGHT@COCOCLUB","5 Nights Stay in a 3 star Hotel. Door to dor shuttle included. Airfare covered.",
                3, VacationPackage.vacationPackageType.HOTEL_AIR,547.98,new Date(2021,01,31),true,false,"bahamas",
                new ArrayList<>());

        VacationPackage vacationPackage_2 = new VacationPackage("HAMPTONS3NIGHT@MARRIOT","3 Nights Stay in a 5 star Resort. Door to dor shuttle included. Airfare covered.",
                3, VacationPackage.vacationPackageType.RESORT,1047.98,new Date(2021,01,31),true,false,"hamptons",
                new ArrayList<>());

        VacationPackage vacationPackage_3 = new VacationPackage("BAHAMAS3NTCRUISE@CARNIVAL","3 Nights Cruise to Bahamas. Door to dor shuttle included. Airfare covered.",
                3, VacationPackage.vacationPackageType.RESORT,947.98,new Date(2021,01,31),true,false,"bahamas",
                new ArrayList<>());

        // 2. Create a Proposal
        int customerReference = 1;

        Proposal proposal = new Proposal(customerReference, vacationPackage_3);
        proposal.addPax("John","","Doe",38);
        proposal.addPax("Jane","","Doe",32);
        proposal.addPax("Jack","","Doe",6);

        // 3. Create the command object v2
        CreateProposalCommand createProposalCommand = new CreateProposalCommand(proposal);

        try {
            createProposalCommand.process();
        } catch(CommandException e){
            e.printStackTrace();
        }

    }
}
