# Statelayout
A flexible, easy to use, all in one state layout library.

## Setup
### Using Gradle
```gradle
compile 'com.whitecloak.statelayout:statelayout:0.1.1@aar'
```
### Using Maven
```xml
<dependency>
  <groupId>com.whitecloak.statelayout</groupId>
  <artifactId>statelayout</artifactId>
  <version>0.1.1</version>
  <type>pom</type>
</dependency>
```

## A quick overview:
- Loading / Content / Error / Empty
- Error / Empty Click Listener
- Define custom layout
- Define custom view by id
- More to come...

## How to use
### Using XML

```XML
<com.whitecloak.statelayout.StateLayout
    android:id="@+id/state_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:contentView="@+id/recycler_view"
    app:layout_behavior="@string/appbar_scrolling_view_behavior">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</com.whitecloak.statelayout.StateLayout>
```

### Switching State
#### Set state to nothing
```java
mStateLayout.showNothing();
```

#### Set state to loading
```java
mStateLayout.showLoading();
```


#### Set state to content
```java
mStateLayout.showContent();
```

#### Set state to error
```java
mStateLayout.showError();
```

#### Set state to error with message
```java
mStateLayout.showError("Something wen't wrong");
```

#### Set state to empty
```java
mStateLayout.showEmpty();
```

#### Set state to empty with error
```java
mStateLayout.showEmpty("No content found");
```

### Listeners
Add action/click listener when state is empty or error

#### Set error listener
```java
stateLayout.setErrorListener(new StateLayout.ActionListener() {
    @Override
    public void onAction() {
        // DO SOMETHING
    }
});
```
#### Set empty listener
```java
stateLayout.setEmptyListener(new StateLayout.ActionListener() {
    @Override
    public void onAction() {
        // DO SOMETHING
    }
});
```
#### Set Error or Empty listener
```java
stateLayout.setErrorEmptyListener(new StateLayout.ActionListener() {
    @Override
    public void onAction() {
        // DO SOMETHING
    }
});
```

# License

    Copyright 2017 White Cloak Technologies, Inc

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.