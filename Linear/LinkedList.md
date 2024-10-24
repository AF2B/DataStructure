# Linked List: From Fundamentals to Advanced Concepts

## Table of Contents
1. [Introduction](#introduction)
2. [Internal Structure](#internal-structure)
3. [Basic Operations](#basic-operations)
4. [Advanced Concepts](#advanced-concepts)
5. [Best Practices](#best-practices)
6. [Performance Considerations](#performance-considerations)
7. [Real-world Examples](#real-world-examples)

## Introduction

A linked list is a fundamental data structure in computer science that consists of a collection of nodes forming a linear sequence. Each node contains a data element and a reference (or pointer) to the next node in the sequence. Unlike arrays, linked lists do not store elements in contiguous memory locations, allowing for dynamic memory allocation and efficient insertions and deletions.

## Internal Structure

There are three main types of linked lists, each with its own characteristics:

1. Singly Linked List:
   - Each node contains data and a reference to the next node
   - Can only be traversed in one direction (forward)
   - Memory efficient due to single reference storage
   - Last node points to null

2. Doubly Linked List:
   - Each node contains data and references to both next and previous nodes
   - Can be traversed in both directions
   - Requires more memory due to additional reference storage
   - Last node's next pointer and first node's previous pointer are null

3. Circular Linked List:
   - Similar to singly or doubly linked lists
   - Last node points back to the first node
   - No null references in the structure
   - Can be traversed indefinitely

## Basic Operations

### Node Structure

```go
type Node struct {
    Data     interface{}
    Next     *Node
    Previous *Node    // Only for doubly linked lists
}
```

### Creating a New List

```go
type LinkedList struct {
    Head   *Node
    Tail   *Node
    Length int
}

func NewLinkedList() *LinkedList {
    return &LinkedList{}
}
```

### Insertion Operations

```go
func (l *LinkedList) InsertAtBeginning(data interface{}) {
    newNode := &Node{Data: data}
    
    if l.Head == nil {
        l.Head = newNode
        l.Tail = newNode
    } else {
        newNode.Next = l.Head
        l.Head = newNode
    }
    l.Length++
}

func (l *LinkedList) InsertAtEnd(data interface{}) {
    newNode := &Node{Data: data}
    
    if l.Head == nil {
        l.Head = newNode
        l.Tail = newNode
    } else {
        l.Tail.Next = newNode
        l.Tail = newNode
    }
    l.Length++
}
```

### Deletion Operations

```go
func (l *LinkedList) DeleteFromBeginning() error {
    if l.Head == nil {
        return errors.New("list is empty")
    }
    
    l.Head = l.Head.Next
    l.Length--
    return nil
}

func (l *LinkedList) DeleteFromEnd() error {
    if l.Head == nil {
        return errors.New("list is empty")
    }
    
    if l.Head.Next == nil {
        l.Head = nil
        l.Tail = nil
    } else {
        current := l.Head
        for current.Next.Next != nil {
            current = current.Next
        }
        current.Next = nil
        l.Tail = current
    }
    l.Length--
    return nil
}
```

## Advanced Concepts

### Traversal with Multiple Pointers

```go
func (l *LinkedList) FindMiddle() *Node {
    if l.Head == nil {
        return nil
    }
    
    slow := l.Head
    fast := l.Head
    
    for fast != nil && fast.Next != nil {
        slow = slow.Next
        fast = fast.Next.Next
    }
    return slow
}
```

### Cycle Detection

```go
func (l *LinkedList) HasCycle() bool {
    if l.Head == nil {
        return false
    }
    
    slow := l.Head
    fast := l.Head
    
    for fast != nil && fast.Next != nil {
        slow = slow.Next
        fast = fast.Next.Next
        if slow == fast {
            return true
        }
    }
    return false
}
```

## Best Practices

1. Always maintain both head and tail pointers for O(1) insertions at both ends
2. Use sentinel nodes when appropriate to simplify edge cases
3. Consider using doubly linked lists when bidirectional traversal is needed
4. Implement proper error handling for edge cases
5. Keep track of list length to avoid unnecessary traversals
6. Use interface{} for generic data types in Go
7. Implement proper cleanup to avoid memory leaks

## Performance Considerations

1. Insertion at beginning/end: O(1)
2. Deletion at beginning: O(1)
3. Deletion at end: O(n) for singly linked list, O(1) for doubly linked list
4. Search: O(n)
5. Access by index: O(n)
6. Memory usage: O(n) with additional overhead per node for references

## Real-world Examples

### Example: Music Playlist Implementation

```go
type Song struct {
    Name     string
    Artist   string
    Previous *Song
    Next     *Song
}

type Playlist struct {
    Name       string
    Head       *Song
    Tail       *Song
    NowPlaying *Song
    Length     int
}

func NewPlaylist(name string) *Playlist {
    if name == "" {
        name = "New Playlist"
    }
    return &Playlist{
        Name: name,
    }
}

func (p *Playlist) AddSong(name, author string) error {
    if name == "" || author == "" {
        return errors.New("invalid song data")
    }

    s := &Song{
        Name:   name,
        Artist: author,
    }
    if p.Head == nil {
        p.Head = s
    } else {
        currentNode := p.Tail
        currentNode.Next = s
        s.Previous = p.Tail
    }
    p.Tail = s
    p.Length++
    return nil
}

func (p *Playlist) ShowAllSongs() error {
    if p.Head == nil {
        return errors.New("playlist is empty")
    }

    currentNode := p.Head
    for currentNode != nil {
        fmt.Printf("%+v\n", *currentNode)
        currentNode = currentNode.Next
    }
    return nil
}

func (p *Playlist) StartPlaying() (*Song, error) {
    if p.Head == nil {
        return nil, errors.New("playlist is empty")
    }

    p.NowPlaying = p.Head
    return p.NowPlaying, nil
}

func (p *Playlist) NextSong() (*Song, error) {
    if p.NowPlaying == nil {
        return nil, errors.New("playlist is empty")
    }

    if p.NowPlaying.Next == nil {
        return nil, errors.New("reached end of playlist")
    }

    p.NowPlaying = p.NowPlaying.Next
    return p.NowPlaying, nil
}

func (p *Playlist) PreviousSong() (*Song, error) {
    if p.NowPlaying == nil {
        return nil, errors.New("playlist is empty")
    }

    if p.NowPlaying.Previous == nil {
        return nil, errors.New("reached beginning of playlist")
    }

    p.NowPlaying = p.NowPlaying.Previous
    return p.NowPlaying, nil
}
```

Usage example:

```go
func main() {
    playlistName := "myplaylist"
    myPlaylist := NewPlaylist(playlistName)
    
    myPlaylist.AddSong("Ophelia", "The Lumineers")
    myPlaylist.AddSong("Shape of you", "Ed Sheeran")
    myPlaylist.AddSong("Stubborn Love", "The Lumineers")
    myPlaylist.AddSong("Feels", "Calvin Harris")
    
    myPlaylist.ShowAllSongs()
    
    song, _ := myPlaylist.StartPlaying()
    fmt.Printf("Now playing: %s by %s\n", song.Name, song.Artist)
    
    song, _ = myPlaylist.NextSong()
    fmt.Printf("Now playing: %s by %s\n", song.Name, song.Artist)
    
    song, _ = myPlaylist.PreviousSong()
    fmt.Printf("Now playing: %s by %s\n", song.Name, song.Artist)
}
```

This implementation demonstrates a practical use of a doubly linked list in a music playlist application, showing how the data structure can be used to navigate through songs both forward and backward, maintain the current playing state, and manage a collection of songs efficiently.