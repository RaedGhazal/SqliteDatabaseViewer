# SqliteDatabaseViewer
This is an android lib just for testing not ready yet And not the full official one.  
The Full one gonna be under the name RGLibrary ,will be a general library which contains many features (not a specific topic) that helps you in your Applications programming.  
if you wanna try this anyways it has 2 features now.

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
                recyclerView.setAdapter(adapter);
```
and That's it.
-----------------------------------------------------
if you like my test lib and wanna get the full official one when it gets published !
email me at <Raed.osama078@gmail.com>
with  
- Title :
> Android-Lib

- Body :
> Hi ,I am Interesting in your library and I would like to have an email when the full one is ready.

Also you can add to the body any features you would like me to add to the full library,  
Or even any question in android programming you want help with.  
I would be honored to help.  
Thank you !

