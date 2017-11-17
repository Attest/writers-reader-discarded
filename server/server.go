package main

import (
	"flag"
	"fmt"
	"net/http"
)

func main() {
	addr := flag.String("addr", ":8000", "service port to run on")
	flag.Parse()

	fmt.Printf("serving on %s\n", *addr)
	http.HandleFunc("/", handleRequest)
	http.ListenAndServe(*addr, nil)
}

func handleRequest(w http.ResponseWriter, req *http.Request) {
	w.Write([]byte("done"))
}
