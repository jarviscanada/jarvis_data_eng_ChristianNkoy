# SQL Query Exercises

### MODIFYING DATA
###### Question 1: 
The club is adding a new facility - a spa. We need to add it into the facilities table. Use the following values: 
* facid: 9, Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.
```sql
insert into cd.facilities 
    values (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2:
Let's try adding the spa to the facilities table again. This time, though, we want to automatically generate the value for the next facid, rather than specifying it as a constant. Use the following values for everything else:
* Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.

```sql
insert into cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    select (select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800;
```
###### Question 3:
We made a mistake when entering the data for the second tennis court. The initial outlay was 10000 rather than 8000: you need to alter the data to fix the error.
```sql
update cd.facilities
	set initialoutlay = 10000
	where facid = 1;
```

###### Question 4:
We want to alter the price of the second tennis court so that it costs 10% more than the first one. Try to do this without using constant values for the prices, so that we can reuse the statement if we want to.
```sql
update cd.facilities facs
	set
	  membercost = (select membercost from cd.facilities where facid = 0) * 1.1,
	  guestcost = (select guestcost from cd.facilities where facid = 0) * 1.1
	where facs.facid = 1;
```

###### Question 5:
As part of a clearout of our database, we want to delete all bookings from the cd.bookings table. How can we accomplish this?
```sql
delete from cd.bookings;
```

###### Question 6:
We want to remove member 37, who has never made a booking, from our database. How can we achieve that?
```sql
delete from cd.members
    where memid = 37;
```
### BASIC
###### Question 7:
How can you produce a list of facilities, with each labelled as 'cheap' or 'expensive' depending on if their monthly maintenance cost is more than $100? Return the name and monthly maintenance of the facilities in question.
```sql
select name,
	case when (monthlymaintenance <= 100) then
		'cheap'
	else
		'expensive'
	end as cost
	from cd.facilities;
```

###### Question 8:
You, for some reason, want a combined list of all surnames and all facility names. Yes, this is a contrived example :-). Produce that list!
```sql
select surname
    from cd.members
union
select name
    from cd.facilities;
```
### JOIN

###### Question 9:
How can you produce a list of the start times for bookings by members named 'David Farrell'?
```sql
select 
  starttime 
from 
  cd.bookings bks 
  inner join cd.members mems on bks.memid = mems.memid 
where 
  mems.firstname = 'David' 
  and mems.surname = 'Farrell';
```

###### Question 10:
How can you produce a list of the start times for bookings for tennis courts, for the date '2012-09-21'? Return a list of start time and facility name pairings, ordered by the time.
```sql
select starttime, name
	from 
		cd.bookings bks
		inner join cd.facilities facs
			on bks.facid = facs.facid
	where
		facs.name in ('Tennis Court 1', 'Tennis Court 2') and
		bks.starttime >= '2012-09-21' and
		bks.starttime < '2012-09-22'
	order by starttime;
```

###### Question 11:
How can you output a list of all members, including the individual who recommended them (if any)? Ensure that results are ordered by (surname, firstname).
```sql
select mems.firstname as memfname, mems.surname as memsname, recs.firstname as recfname, recs.surname as recsname
	from 
		cd.members mems
		left outer join cd.members recs
			on mems.recommendedby = recs.memid
	
order by mems.surname, mems.firstname;
```

###### Question 12:
How can you output a list of all members who have recommended another member? Ensure that there are no duplicates in the list, and that results are ordered by (surname, firstname).
```sql
select distinct recs.firstname as firstname, recs.surname as surname
	from 
		cd.members mems
		inner join cd.members recs
			on recs.memid = mems.recommendedby
order by recs.surname, recs.firstname;
```

### AGGREGATION

###### Question 13:
Produce a count of the number of recommendations each member has made. Order by member ID.
```sql
select recommendedby, count(*)
	from cd.members
	where recommendedby is not null
	group by recommendedby
order by recommendedby;
```

###### Question 14:
Produce a list of the total number of slots booked per facility. For now, just produce an output table consisting of facility id and slots, sorted by facility id.
```sql
select facid, sum(slots) as "Total Slots"
	from cd.bookings
	group by facid
order by facid;
```

###### Question 15:
Produce a list of the total number of slots booked per facility in the month of September 2012. Produce an output table consisting of facility id and slots, sorted by the number of slots.
```sql
select facid, sum(slots) as "Total Slots"
	from cd.bookings
	where 
		starttime >= '2012-09-01' and
		starttime < '2012-10-01'
	group by facid
order by "Total Slots";
```

###### Question 16:
Produce a list of the total number of slots booked per facility per month in the year of 2012. Produce an output table consisting of facility id and slots, sorted by the id and month.
```sql
select facid, extract(month from starttime) as month, sum(slots) as "Total Slots"
	from cd.bookings
	where extract(year from starttime) = 2012
	group by facid, month
order by facid, month;
```

###### Question 17:
Find the total number of members (including guests) who have made at least one booking.
```sql
select count (distinct memid) from cd.bookings;
```

###### Question 18:
Produce a list of each member name, id, and their first booking after September 1st 2012. Order by member ID.
```sql
select mems.surname, mems.firstname, mems.memid, min(bks.starttime) as starttime
	from cd.bookings bks
		inner join cd.members mems
			on mems.memid = bks.memid
	where starttime > '2012-09-01'
	group by mems.surname, mems.firstname, mems.memid
order by mems.memid;
```

#### Window Functions
###### Question 19:
Produce a list of member names, with each row containing the total member count. Order by join date, and include guest members.
```sql
select count(*) over(), firstname, surname
	from cd.members
order by joindate;
```

###### Question 20:
Produce a monotonically increasing numbered lisProduce a monotonically increasing numbered liProduce a monotonically increasing numbered list of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.st of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.t of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.
```sql
select count(*) over(order by joindate), firstname, surname
	from cd.members
order by joindate
```

###### Question 21:
Output the facility id that has the highest number of slots booked. Ensure that in the event of a tie, all tieing results get output.
```sql
select facid, total from (
	select facid, sum(slots) as total, rank() over (order by sum(slots) desc) as rank
  		from cd.bookings
  		group by facid
  	) as ranked
	
  where rank = 1;
```

### STRING

###### Question 22:
Output the names of all members, formatted as 'Surname, Firstname'
```sql
select surname || ', ' || firstname as name from cd.members;
```

###### Question 23:
Perform a case-insensitive search to find all facilities whose name begins with 'tennis'. Retrieve all columns.
```sql
select *
	from cd.facilities
	where upper(name) like 'TENNIS%'; 
```

###### Question 24:
You've noticed that the club's member table has telephone numbers with very inconsistent formatting. You'd like to find all the telephone numbers that contain parentheses, returning the member ID and telephone number sorted by member ID.
```sql
select memid, telephone
	from cd.members
	where telephone ~ '[()]'
order by memid;
```

###### Question 25:
You'd like to produce a count of how many members you have whose surname starts with each letter of the alphabet. Sort by the letter, and don't worry about printing out a letter if the count is 0.
```sql
select substr(surname, 1, 1) as letter, count(*)
  	from cd.members
	group by letter
	order by letter;
```