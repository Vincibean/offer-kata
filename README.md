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

## Assumptions
- No frontend development is necessary;
- Offers are modeled as "the place where merchants meet products": a merchant can propose products via offers only; this
means that the same merchant can sell the same product using different offers (and, as such, prices); this should allow
quite an high degree of flexibility;   
- In order to provide a validity to an offer, we could have used a date (e.g. valid till the 9th of August) or a period 
(e.g. valid for the next 3 weeks): we preferred the date approach, as we deemed it more intuitive and user-friendly;
- We don't need a fine-grained approach to dates (e.g. valid till the 17:00 of the 9th of August);
- canceling a valid offer means that, from the given moment, that offer isn't valid anymore;
- canceling an already invalid offer must trigger an error message;
- We didn't deal with timezones: we give for granted that the fronted will deal with that; 
- When deserializing a JSON containing a currency, if the currency is unknown, we default it to USD 
(currencies from all over the world are covered, though);
- Error messages aren't localized; 
- We use JSON for requests/responses: it can be safely considered the standard nowadays;