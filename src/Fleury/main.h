#ifndef MAIN_H
#define MAIN_H

#include <QApplication>
#include <string>
#include <iostream>

/*
Edge structure with all needed information
To do
*/
struct edge{
int x;
}TEdge;


/*
Vertex structure with all needed information
To do
*/
struct vertex {
std::string name;
int edge_count;
struct Tvertex** adject_vertexes;
}Tvertex;

#endif // MAIN_H
