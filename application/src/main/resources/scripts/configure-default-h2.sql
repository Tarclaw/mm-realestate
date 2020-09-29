INSERT INTO clients(id, email, mobile_number, skype, first_name, last_name, login, password, customer_requirements) VALUES(3, 'r.rich@gmail.com', '+1-097-999-3355', 'r.rich', 'Richie', 'Rich', 'dineiro_login', 'babosiki_pass', 'sober, not a student');
INSERT INTO clients(id, email, mobile_number, skype, first_name, last_name, login, password, customer_requirements) VALUES(4, 'd.donaldson@gmail.com', '+1-066-534-6842', 'd.donaldson', 'Donald', 'Donaldson', 'duck_login', 'kr9kr9', 'young student female please');
INSERT INTO clients(id, email, mobile_number, skype, first_name, last_name, login, password, customer_requirements) VALUES(5, 'j.johnson@gmail.com', '+1-099-897-3511', 'j.johnson', 'John', 'Johnson', 'johny_login', 'rieltoruzlo', 'no cats, dogs, etc');

INSERT INTO real_estate_agents(id, email, mobile_number, skype, first_name, last_name, login, password, hired_date, salary) VALUES(1, 'm.public@realestate.com', '+1-014-777-3355', 'm.public', 'Marry', 'Public', 'marry_login', '123456', '2020-04-27', 5000.00);
INSERT INTO real_estate_agents(id, email, mobile_number, skype, first_name, last_name, login, password, hired_date, salary) VALUES(2, 'b.billy@realestate.com', '+1-014-515-2288', 'b.billy', 'Billy', 'Butkiss', 'billy_login', 'qwerty', '2020-04-27', 4500.00);

INSERT INTO facilities(id, description, number_of_rooms, published_date_time, total_area, month_rent, price, status, client_id) VALUES(11, 'top rated xata', 5, '2020-04-27 19:53:26.298205', 150, 4000.00, 1000000.00, 'FOR_RENT', 4);
INSERT INTO apartments(id, apartment_number, floor) VALUES(11, 788, 25);

INSERT INTO facilities(id, description, number_of_rooms, published_date_time, total_area, month_rent, price, status, client_id) VALUES(12, 'barbershop, ready business model', 3, '2020-04-27 19:53:26.298205', 200, 4200.00, 1500000.00, 'FOR_SALE', 3);
INSERT INTO basements(id, it_commercial) VALUES(12, true);

INSERT INTO facilities(id, description, number_of_rooms, published_date_time, total_area, month_rent, price, status, client_id) VALUES(13, 'vehicles repair', 3, '2020-04-27 19:53:26.298205', 600, 6000.00, 2000000.00, 'FOR_SALE', 3);
INSERT INTO garages(id, has_equipment, has_pit) VALUES(13, true, true);

INSERT INTO facilities(id, description, number_of_rooms, published_date_time, total_area, month_rent, price, status, client_id) VALUES(14, 'new home with repair for big family', 8, '2020-04-27 19:53:26.298205', 300, 3000.00, 700000.00, 'RENTED', 4);
INSERT INTO houses(id, has_backyard, has_garden, number_of_storeys) VALUES(14, true, true, 3);

INSERT INTO facilities(id, description, number_of_rooms, published_date_time, total_area, month_rent, price, status, client_id) VALUES(15, 'huge storage for drug dealers', 3, '2020-04-27 19:53:26.298205', 700, 5000.00, 1200000.00, 'SOLD', 5);
INSERT INTO storages(id, commercial_capacity, has_cargo_equipment) VALUES(15, 600, true);

INSERT INTO addresses(city, district, facility_number, postcode, street, facility_id) VALUES('New York', 'NY', 255, 100305, 'Manhattan', 15);
INSERT INTO addresses(city, district, facility_number, postcode, street, facility_id) VALUES('New York', 'NY', 28, 55322, 'Madison Ave', 14);
INSERT INTO addresses(city, district, facility_number, postcode, street, facility_id) VALUES('New York', 'NY', 143, 200645, 'Bronx', 13);
INSERT INTO addresses(city, district, facility_number, postcode, street, facility_id) VALUES('New York', 'NY', 657, 99887, 'Brooklyn', 12);
INSERT INTO addresses(city, district, facility_number, postcode, street, facility_id) VALUES('New York', 'NY', 665, 888132, 'Queens', 11);

INSERT INTO clients_agents(agent_id, client_id) VALUES(1, 3);
INSERT INTO clients_agents(agent_id, client_id) VALUES(1, 4);
INSERT INTO clients_agents(agent_id, client_id) VALUES(2, 5);
