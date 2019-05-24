To search for all publications by organization egLASP publications:
{"query":{"bool":{"must":[{"term":{"authors.organization.name.keyword":"Laboratory for Atmospheric and Space Physics (LASP)"}},{"match_all":{}}]}},"sort":[{"publicationYear.keyword":{"order":"desc"}}],"from":0,"size":20}

This query can be translated to this URL encoded string which can be used on the browser
https://experts.colorado.edu/es/webex-v3/_search?source_content_type=application%2Fjson&source=%7B%22query%22%3A%7B%22bool%22%3A%7B%22must%22%3A%5B%7B%22term%22%3A%7B%22authors.organization.name.keyword%22%3A%22Laboratory%20for%20Atmospheric%20and%20Space%20Physics%20(LASP)%22%7D%7D%2C%7B%22match_all%22%3A%7B%7D%7D%5D%7D%7D%2C%22sort%22%3A%5B%7B%22publicationYear.keyword%22%3A%7B%22order%22%3A%22desc%22%7D%7D%5D%2C%22from%22%3A0%2C%22size%22%3A20%7D

Mappings showing list of all fields in the index:
https://experts.colorado.edu/es/webex-v3/_mapping

Javascript library we use to build our people and publcations page from Elastic:
https://github.com/vivo-community/facetedsearch
