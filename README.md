# Offer Kata
## Background
> "an offer is a proposal to sell a specific product or
  service under specific conditions"
  
  -- Wikipedia
  
Merchants offer goods for sale; they can create offers to share with their customers.

All the offers have shopper friendly descriptions. Offers are priced up front in a defined currency.

An offer is time-bounded, with the length of time an offer is valid for defined as part of the offer, and should expire automatically. 
Offers may also be explicitly cancelled before they expire.

## Assignment
You are required to create a simple RESTful software service that will allow a merchant to create a new simple offer. 
Offers, once created, may be queried. After the period of time defined on the offer it should expire and
further requests to query the offer should reflect that somehow. 
Before an offer has expired users may cancel it.