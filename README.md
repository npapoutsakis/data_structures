# File Format Data Structures

This project demonstrates various file format techniques and search algorithms for efficient data storage and retrieval. It implements four different file format methods and compares their performance using disk access metrics.

## Overview

The system creates and manages files with random data nodes, implements indexing strategies, and provides search algorithms to efficiently retrieve data. The project focuses on demonstrating the performance differences between different file organization techniques and search methods.

## File Format Types

The project implements four different file format methods:

- **Type A**: Serial unordered file with DataNodes
- **Type B**: Serial unordered file with IndexNodes
- **Type C**: Sorted file with DataNodes (created by external sorting)
- **Type D**: Sorted file with IndexNodes (created by external sorting)

## Search Algorithms

Two search algorithms are implemented to compare performance:

- **Array Search**: Linear search through files
- **Binary Search**: Applied to sorted files for improved performance

## Project Structure

### Core Interfaces
- `FileCreator`: Interface for creating data and index files
- `Node`: Base interface for all node types
- `Search`: Interface for search algorithms
- `Tester`: Interface for testing search performance

### File Management
- `FileManager`: Handles low-level file operations (create, read, write, append)

### Node Types
- `SimpleNode`: Base implementation of Node interface
- `DataNode`: Extends SimpleNode with additional string data
- `IndexNode`: Extends SimpleNode with index information

### File Creators
- `SerializedFileCreator`: Creates unsorted files (Type A and B)
- `SortedFileCreator`: Creates sorted files using external sorting (Type C and D)

### Search Implementations
- `ArraySearch`: Implements linear search on files
- `BinarySearch`: Implements binary search on sorted files

### Testing
- `ArraySearchTester`: Tests performance of linear search
- `BinarySearchTester`: Tests performance of binary search

## Key Features

- Random node generation with keys from 1 to 10^6
- File organization with fixed page and node sizes
- External sorting of files
- Performance measurement through disk access counting
- Index-based and direct data file searching
- Binary search optimization for sorted files

## Configuration

The system uses the following configuration parameters:

- Number of nodes: 10,000
- Data page size: 128 bytes
- Data node size: 32 bytes
- Index node size: 8 bytes
- Test search operations: 20 per test

## Usage

The main entry point is `Main.java` which demonstrates:

1. Creation of type A and B files
2. Array search testing on these files
3. Creation of type C and D files through external sorting
4. Binary search testing on the sorted files
5. Performance comparison between different file formats and search methods

## Performance Metrics

Performance is measured by counting disk accesses during:
- File creation and sorting operations
- Search operations using different algorithms

The results are displayed showing the average number of disk accesses required for each file format type and search method.

## Requirements

- Java Development Kit (JDK)
- No external dependencies required
