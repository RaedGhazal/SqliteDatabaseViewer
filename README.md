# SqliteDatabaseViewer
This is an android library just for testing during the development of your app. 

### First : 
### Viewing Your SQliteDatabase ->

1.Create new Activity (You can delete its XML file) and add this code into your ``` @Override onCreate```
```
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RGDatabaseViewer rgDatabaseViewer 
                              = new RGDatabaseViewer(YourActivity.this,new DatabaseHelper(YourActivity.this));
        setContentView(rgDatabaseViewer.getView());
    }
```
> Note !! the DatabaseHelper is the class that extends from SQLiteOpenHelper and contains the onCreate , onUpate functions.

2.Just Create a simple ```Intent``` to the Activity and that's it .


### Second :
### Use my RecyclerView Adapter that can fit any amount of data and Columns -> 

1.Create a simple RecyclerView.
2.Initialize it into your Activity.java
3.Add your LayoutManager.
4.set the adapter as the code below : 

```
RecyclerView recyclerView = findViewById(R.id.recyclerView);
ArrayList<String> headerList = new ArrayList();
ArrayList<String[]> dataList = new ArrayList();
/*
add data to your headerList and dataList
*/

RGRecyclerViewAdapter rgRecyclerViewAdapter = new RGRecyclerViewAdapter(YourActivity.this,headerList,dataList);
                RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(rgRecyclerViewAdapter);
```
and That's it.
-----------------------------------------------------
