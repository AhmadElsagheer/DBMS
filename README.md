# DBApp

Database Management System

#Assignment 1: Database engine

#Upcoming submissions
Deletion of data will result in fragmentation within a page; do not worry about that in your
solution.

Each table should have an additional column beside those specified by the user. The additional
column must be called TouchDate and is initialized with the date/time of row insertion and
updated with current date/time every time a row is updated.

Indices
12) You are required to use B+ trees to support creating primary and secondary dense indices.
Note that a B+ tree stores in its’ leafs pointers (handles in Java terminology) to the data objects.
13) You are going to implement your own B+ Tree data structure.
14) Once a table is created, you need to create a primary index on the key of that table.
15) You should update existing relevant B+ trees when a tuple is inserted/deleted.
16) If a secondary index is created after a table has been populated, you have no option but to
scan the whole table.
17) Upon application startup; to avoid having to scan all tables to build existing indices, you
should save the index itself to disk and load it when the application starts next time.
18) If a select is executed (by calling selectFromTable method below), and a column is
referenced in the select that has been already indexed, then you should search in the index.

Note that because you are using dense indices, the location of where to actually insert a tuple in
a table does not matter as the B+ tree will refer to the page/line of that tuple. Neither it matters
whether the table/page is sorted or not.

Deletion of a tuple has to be managed carefully. If you delete the line containing the tuple, it
will affect the location of lines which follows in that page. Hence, it is better to use a tomb stone
to indicate that the tuple has been deleted without actually removing the line.
