-- Some insert statements to give an idea

INSERT INTO customers (email_address,fname,mname,lname,phone)
    VALUES ('abc@xyz.com','John','','Doe','7325555555');

INSERT INTO customers (email_address,fname,mname,lname,phone)
    VALUES ('andy@vvv.com','Andy','S','Knoworth','9125555555');

INSERT INTO customers (email_address,fname,mname,lname,phone)
    VALUES ('Don@ttt.com','Donegan','','Woolworth','3175555555');

-- Lets assume that there are proposals created for the customers

-- Proposals for John
INSERT INTO proposals(customer_id, package_id, pax_0,pax_age_0, pax_1,pax_age_1)
       VALUES (1, 'BAHAMA5NIGHT@COCOCLUB','John--Doe',42,'Jan--Doe', 39);
INSERT INTO proposals(customer_id, package_id, pax_0,pax_age_0, pax_1,pax_age_1)
       VALUES (1, 'BAHAMA3NIGHT@MARRIOT','John--Doe',42,'Jane--Doe', 39);


-- Proposals for Andy
INSERT INTO proposals(customer_id, package_id, pax_0,pax_age_0, pax_1,pax_age_1, pax_2,pax_age_2)
       VALUES (2, 'CARNIVALCRUISE3NIGHT@NEW_ENGLAND','Andy--K',52, 'Nicole--K',45, 'Jim--K', 16 );
INSERT INTO proposals(customer_id, package_id, pax_0,pax_age_0, pax_1,pax_age_1, pax_2,pax_age_2)
       VALUES (2, 'NORWAGIAN3NIGHT@BAHAMAS','Andy--K',52, 'Nicole--K',45, 'Jim--K', 16 );
INSERT INTO proposals(customer_id, package_id, pax_0,pax_age_0, pax_1,pax_age_1, pax_2,pax_age_2)
       VALUES (2, 'BAHAMA5NIGHT@COCOCLUB','Andy--K',52, 'Nicole--K',45, 'Jim--K', 16 );
