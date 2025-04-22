# **Selector Util API**

This repository contains the logic for the Selector Util API. It defines the core classes and interfaces for creating 
and managing selectors with varying probabilities.

### Overview

The Selector Util API provides an abstraction for selecting random elements from a collection. 
It supports different types of selectors, each with unique behaviors and configurations that determine the selection 
process. 
The API allows for dynamic adjustment of weights, which influences the probability of selecting a particular element.

### Features

- **AbstractRandomSelector**: A base class for creating random selectors.
- **RandomSelector**: Implements a selector with basic randomness.
- **RationalRandomSelector**: A selector that uses a rational approach to manage probabilities.
- **VariantsCollection**: A collection class for managing elements in selectors.
- **VariantColorPalette**: Generates color palettes for visual representation of variants.

## Prerequisites

- **Java 11+**: Ensure you have Java Development Kit version 11 or higher.
- **Maven**: This project uses Maven for dependency management and building.


### Getting Started & Installation

To use the Selector Util API, include it as a module in your Java project.
Clone the repository and build it using Maven:

```bash
git clone https://github.com/yourusername/selector-util-api.git
cd selector-util-api
mvn clean install
```
